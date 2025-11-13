package com.campustable.be.domain.auth.service;

import com.campustable.be.domain.User.repository.UserRepository;
import com.campustable.be.domain.User.entity.User;
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
import java.util.HashMap;
import java.util.Map;
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
   * 통합 로그인 처리 (DB 우선 조회)
   * 1. DB에서 학번으로 사용자 조회
   * 2. 기존사용자와 신규사용자로 분기
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

  private AuthResponse handleExistingUser(LoginRequest loginRequest, User existingUser) throws IOException {
    log.info("기존 사용자 로그인 시도: {}", loginRequest.getSejongPortalId());

    Map<String, Object> claims = new HashMap<>();
    String refreshTokenId = UUID.randomUUID().toString();
    String accessTokenId = UUID.randomUUID().toString();
    sejongPortalLoginService.validateLogin(loginRequest);

    String accessToken = jwtProvider.createAccessToken(existingUser, accessTokenId);
    String refreshToken = jwtProvider.createRefreshToken(existingUser, refreshTokenId);

    RefreshToken refresh = setRefreshToken(refreshToken, existingUser.getUserId());

    refreshTokenRepository.save(refresh);

    return AuthResponse.builder()
        .studentNumber(loginRequest.getSejongPortalId())
        .studentName(null) // 기존 사용자는 이름 조회 안 함 (성능 최적화)
        .isNewUser(false)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .maxAge(jwtProvider.getRefreshInMs()/1000)
        .build();
  }

  public AuthResponse handleNewUser(LoginRequest loginRequest) throws IOException {
    log.info("신규 사용자 온보딩 시도: {}", loginRequest.getSejongPortalId());

    Map<String, Object> claims = new HashMap<>();
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
    String accessTokenId = UUID.randomUUID().toString();
    log.info("신규 사용자 DB 저장 완료: {} {}", user.getStudentNumber(), user.getUserName());

    String accessToken = jwtProvider.createAccessToken(user, accessTokenId);
    String refreshToken = jwtProvider.createRefreshToken(user, refreshTokenId);

    RefreshToken refresh = setRefreshToken(refreshTokenId, user.getUserId());
    refreshTokenRepository.save(refresh);

    return AuthResponse.builder()
        .studentNumber(newUser.getStudentNumber())
        .studentName(memberInfo.getStudentName())
        .isNewUser(true)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .maxAge(jwtProvider.getRefreshInMs()/1000)
        .build();
  }

  public TokenReissueResponse reissueToken(String refreshToken) {

    String jti = jwtProvider.getJti(refreshToken);

    RefreshToken existingRefreshToken = refreshTokenRepository.findById(jti)
        .orElseThrow(()->{
          log.error("redis에 refreshToken이존재하지 않습니다.");
          throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        });
    Long userId = existingRefreshToken.getUserId();
    refreshTokenRepository.delete(existingRefreshToken);

    User user = userRepository.findById(userId)
        .orElseThrow(()->{
          log.error("refreshToken에 해당하는 유저가 존재하지않습니다.");
          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    String newJti = UUID.randomUUID().toString(); //refreshtoekn저장할때 jti를 공유해야해서
    String newAccessToken =  jwtProvider.createAccessToken(user,newJti);
    String newRefreshToken =  jwtProvider.createRefreshToken(user,newJti);
    refreshTokenRepository.save(setRefreshToken(newJti, userId));
    return new TokenReissueResponse(newAccessToken, newRefreshToken,
        jwtProvider.getRefreshInMs()/1000);
  }


  private RefreshToken setRefreshToken(String jti, Long userId) {
    return RefreshToken.builder()
        .jti(jti)
        .expiration(jwtProvider.getRefreshInMs()/1000)
        .userId(userId)
        .build();
  }
}



