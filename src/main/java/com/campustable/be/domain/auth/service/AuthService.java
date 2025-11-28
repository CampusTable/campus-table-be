package com.campustable.be.domain.auth.service;

import com.campustable.be.domain.user.repository.UserRepository;
import com.campustable.be.domain.user.entity.User;
import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.domain.auth.dto.LoginRequest;
import com.campustable.be.domain.auth.dto.SejongMemberInfo;
import com.campustable.be.domain.auth.dto.TokenReissueResponse;
import com.campustable.be.domain.auth.entity.RefreshToken;
import com.campustable.be.domain.auth.provider.JwtProvider;
import com.campustable.be.domain.auth.repository.RefreshTokenRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

  private final UserRepository userRepository;
  private final SejongPortalLoginService sejongPortalLoginService;
  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  /**
   * Handle unified login by looking up the user by student number and delegating to either existing-user login or onboarding.
   *
   * @param loginRequest request carrying the Sejong portal ID used as the student number and any credentials required for portal verification
   * @return an AuthResponse containing user identity, an `isNewUser` flag, and generated access/refresh tokens with their expiry information
   * @throws IOException if an I/O error occurs during portal verification or onboarding operations
   */

  public AuthResponse login(LoginRequest loginRequest) throws IOException {
    String studentNumber = loginRequest.getSejongPortalId();

    // 1. DB 우선 조회
    Optional<User> existingUser = userRepository.findByStudentNumber(studentNumber);

    if (existingUser.isPresent()) {
      // 2-A. 기존 사용자 처리 (Effective Login)
      return handleExistingUser(loginRequest, existingUser.get());
    } else {
      // 2-B. 신규 사용자 처리 (Onboarding Transaction)
      return handleNewUser(loginRequest);
    }
  }

  /**
   * Creates an authentication response for an existing user by validating the provided Sejong portal credentials,
   * issuing a new access token and refresh token, persisting the refresh token, and returning token details.
   *
   * @param loginRequest  the incoming login request containing the Sejong portal ID and credentials
   * @param existingUser  the persisted user that matches the loginRequest's student identifier
   * @return              an AuthResponse populated with studentNumber, studentName, isNewUser=false, accessToken,
   *                      refreshToken, and the refresh token lifetime in seconds
   * @throws IOException  if Sejong portal validation or related I/O operations fail
   */
  private AuthResponse handleExistingUser(LoginRequest loginRequest, User existingUser) throws IOException {

    String refreshTokenId = UUID.randomUUID().toString();
    sejongPortalLoginService.validateLogin(loginRequest);

    String accessToken = jwtProvider.createAccessToken(existingUser);
    String refreshToken = jwtProvider.createRefreshToken(existingUser, refreshTokenId);

    RefreshToken refresh = setRefreshToken(refreshTokenId, existingUser.getUserId());

    refreshTokenRepository.save(refresh);

    return AuthResponse.builder()
        .studentNumber(loginRequest.getSejongPortalId())
        .studentName(existingUser.getUserName())
        .isNewUser(false)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .maxAgeSeconds(jwtProvider.getRefreshInMs()/1000)
        .build();
  }

  /**
   * Onboards a new user and returns authentication tokens together with the user's basic info.
   *
   * @param loginRequest the login request containing Sejong portal credentials/identifier used to verify and obtain member information
   * @return an AuthResponse containing the user's student number and name, `isNewUser` set to `true`, a new access token, a new refresh token, and `maxAgeSeconds` indicating the refresh token lifetime in seconds
   * @throws IOException if verifying credentials or retrieving member information from the Sejong portal fails
   */
  public AuthResponse handleNewUser(LoginRequest loginRequest) throws IOException {
    log.info("신규 사용자 온보딩 시도: {}", loginRequest.getSejongPortalId());

    // 외부 포털 API로 ID/PW 검증 + 학생 정보 파싱
    SejongMemberInfo memberInfo = sejongPortalLoginService.loginAndGetMemberInfo(loginRequest);

    // 트랜잭션 내에서 DB 저장
    User newUser = User.builder()
        .studentNumber(memberInfo.getStudentNumber())
        .userName(memberInfo.getStudentName())
        .role("USER")
        .build();

    User user = userRepository.save(newUser);
    String refreshTokenId = UUID.randomUUID().toString();
    log.info("신규 사용자 DB 저장 완료: {} {}", user.getStudentNumber(), user.getUserName());

    String accessToken = jwtProvider.createAccessToken(user);
    String refreshToken = jwtProvider.createRefreshToken(user, refreshTokenId);

    RefreshToken refresh = setRefreshToken(refreshTokenId, user.getUserId());
    refreshTokenRepository.save(refresh);

    return AuthResponse.builder()
        .studentNumber(newUser.getStudentNumber())
        .studentName(memberInfo.getStudentName())
        .isNewUser(true)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .maxAgeSeconds(jwtProvider.getRefreshInMs()/1000)
        .build();
  }

  /**
   * Rotates the provided refresh token and returns newly issued access and refresh tokens.
   *
   * <p>Extracts the token identifier (jti) from the given refresh token, validates and removes the
   * stored refresh token, issues a new access token and a new refresh token for the associated
   * user, persists the new refresh token record, and returns the tokens with their refresh expiry.</p>
   *
   * @param refreshToken the current refresh JWT to reissue from
   * @return a TokenReissueResponse containing the new access token, new refresh token, and refresh expiry in seconds
   */
  public TokenReissueResponse reissueToken(String refreshToken) {

    String jti = jwtProvider.getJti(refreshToken);

    RefreshToken existingRefreshToken = refreshTokenRepository.findById(jti)
        .orElseThrow(()->{
          log.error("redis에 refreshToken이존재하지 않습니다.");
          return new CustomException(ErrorCode.JWT_INVALID);
        });
    Long userId = existingRefreshToken.getUserId();
    refreshTokenRepository.delete(existingRefreshToken);

    User user = userRepository.findById(userId)
        .orElseThrow(()->{
          log.error("refreshToken에 해당하는 유저가 존재하지않습니다.");
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    String refreshTokenId = UUID.randomUUID().toString();
    String newAccessToken =  jwtProvider.createAccessToken(user);
    String newRefreshToken =  jwtProvider.createRefreshToken(user,refreshTokenId);
    refreshTokenRepository.save(setRefreshToken(refreshTokenId, userId));
    return new TokenReissueResponse(newAccessToken, newRefreshToken,
        jwtProvider.getRefreshInMs()/1000);
  }


  /**
   * Builds a RefreshToken entity for the given user using the provided JTI.
   *
   * @param jti    the unique token identifier (JTI) for the refresh token
   * @param userId the id of the user the refresh token will be associated with
   * @return       a RefreshToken entity containing the provided `jti`, `userId`, and an expiration (in seconds) derived from the JWT provider's refresh expiry
   */
  public RefreshToken setRefreshToken(String jti, Long userId) {
    return RefreshToken.builder()
        .jti(jti)
        .expiration(jwtProvider.getRefreshInMs()/1000)
        .userId(userId)
        .build();
  }
}


