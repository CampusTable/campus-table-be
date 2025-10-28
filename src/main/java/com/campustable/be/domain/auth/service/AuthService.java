package com.campustable.be.domain.auth.service;

import com.campustable.be.domain.User.Repository.UserRepository;
import com.campustable.be.domain.User.entity.User;
import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.domain.auth.dto.LoginRequest;
import com.campustable.be.domain.auth.dto.SejongMemberInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  private final SejongPortalLoginService sejongPortalLoginService;

  /**
   * 통합 로그인 처리 (DB 우선 조회)
   * 1. DB에서 학번으로 사용자 조회
   * 2. 기존사용자와 신규사용자로 분기
   */
  @Transactional
  public AuthResponse login(LoginRequest loginRequest) throws IOException {
    String studentNumber = loginRequest.getSejongPortalId();

    // 1. DB 우선 조회
    Optional<User> existingUser = userRepository.findByStudentNumber(studentNumber);

    if (existingUser.isPresent()) {
      // 2-A. 기존 사용자 처리 (Effective Login)
      return handleExistingUser(loginRequest);
    } else {
      // 2-B. 신규 사용자 처리 (Onboarding Transaction)
      return handleNewUser(loginRequest);
    }
  }

  /**
   * 기존 사용자 처리 (Effective Login)
   * - ID/PW 검증만 수행 (빠른 검증)
   */
  private AuthResponse handleExistingUser(LoginRequest loginRequest) throws IOException {
    log.info("기존 사용자 로그인 시도: {}", loginRequest.getSejongPortalId());

    // 외부 포털 API로 ID/PW 검증만 수행
    sejongPortalLoginService.validateLogin(loginRequest);

    return AuthResponse.builder()
        .studentNumber(loginRequest.getSejongPortalId())
        .studentName(null) // 기존 사용자는 이름 조회 안 함 (성능 최적화)
        .isNewUser(false)
        .build();
  }

  /**
   * 신규 사용자 처리 (Onboarding Transaction)
   * - ID/PW 검증 + 학생 정보 파싱
   * - 성공 시 DB 저장
   */
  @Transactional
  public AuthResponse handleNewUser(LoginRequest loginRequest) throws IOException {
    log.info("신규 사용자 온보딩 시도: {}", loginRequest.getSejongPortalId());

    // 외부 포털 API로 ID/PW 검증 + 학생 정보 파싱
    SejongMemberInfo memberInfo = sejongPortalLoginService.loginAndGetMemberInfo(loginRequest);

    // 트랜잭션 내에서 DB 저장
    User newUser = new User();
    newUser.setStudentNumber(memberInfo.getStudentId());
    newUser.setRole("USER");
    newUser.setUserName(memberInfo.getStudentName());

    userRepository.save(newUser);
    log.info("신규 사용자 DB 저장 완료: {} {}", newUser.getStudentNumber(), newUser.getUserName());

    // 토큰 생성

    // RefreshToken은 AuthResponse에 포함하지 않음 (컨트롤러에서 쿠키로 처리)
    return AuthResponse.builder()
        .studentNumber(newUser.getStudentNumber())
        .studentName(memberInfo.getStudentName())
        .isNewUser(true)
        .build();
  }

}



