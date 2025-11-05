package com.campustable.be.domain.auth.service;

import com.campustable.be.domain.User.repository.UserRepository;
import com.campustable.be.domain.User.entity.User;
import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.domain.auth.dto.LoginRequest;
import com.campustable.be.domain.auth.dto.SejongMemberInfo;
import com.campustable.be.domain.auth.provider.JwtProvider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

  /**
   * 기존 사용자 처리 (Effective Login)
   * - ID/PW 검증만 수행 (빠른 검증)
   */
  private AuthResponse handleExistingUser(LoginRequest loginRequest, User existingUser) throws IOException {
    log.info("기존 사용자 로그인 시도: {}", loginRequest.getSejongPortalId());

    Map<String, Object> claims = new HashMap<>();

    sejongPortalLoginService.validateLogin(loginRequest);

    String studentNumber = loginRequest.getSejongPortalId();
    claims.put("role", existingUser.getRole());
    String accessToken = jwtProvider.createToken(studentNumber, claims);
    String refreshToken = jwtProvider.createRefreshToken(studentNumber);
    //refreshToken redis에 저장할게용 나중에 구현해야해!
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
    User newUser = new User();
    newUser.setStudentNumber(memberInfo.getStudentId());
    newUser.setRole("USER");
    newUser.setUserName(memberInfo.getStudentName());

    claims.put("role", newUser.getRole());

    userRepository.save(newUser);
    log.info("신규 사용자 DB 저장 완료: {} {}", newUser.getStudentNumber(), newUser.getUserName());

    String accessToken = jwtProvider.createToken(newUser.getStudentNumber(), claims);
    String refreshToken = jwtProvider.createRefreshToken(newUser.getStudentNumber());
    //refreshToken redis에 저장할게용 나중에 구현해야해!

    return AuthResponse.builder()
        .studentNumber(newUser.getStudentNumber())
        .studentName(memberInfo.getStudentName())
        .isNewUser(true)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .maxAge(jwtProvider.getRefreshInMs()/1000)
        .build();
  }

}



