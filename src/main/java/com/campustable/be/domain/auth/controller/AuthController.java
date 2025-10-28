package com.campustable.be.domain.auth.controller;

import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.domain.auth.dto.LoginRequest;
import com.campustable.be.domain.auth.service.AuthService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  /**
   * 통합 로그인 API
   * - DB 조회 → 기존/신규 사용자 분기 처리
   */
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws IOException {

    AuthResponse authResponse = authService.login(loginRequest);

    log.info("로그인 성공 - 학번: {}, 신규유저: {}",
        authResponse.getStudentNumber(), authResponse.isNewUser());
    return ResponseEntity.ok(authResponse);
  }
}
