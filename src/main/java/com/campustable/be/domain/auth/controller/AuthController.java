package com.campustable.be.domain.auth.controller;

import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.domain.auth.dto.LoginRequest;
import com.campustable.be.domain.auth.dto.TokenReissueResponse;
import com.campustable.be.domain.auth.service.AuthService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  /**
   * Authenticate a user and establish a refresh-token cookie for subsequent requests.
   *
   * Processes the login request, issues authentication tokens, and sets the refresh token as an
   * HTTP-only, Secure, SameSite=Strict cookie on the response; the returned AuthResponse has its
   * refreshToken field cleared.
   *
   * @param loginRequest the login credentials or provider token supplied in the request body
   * @return the authentication response containing user information and flags (with `refreshToken` cleared)
   * @throws IOException if an I/O error occurs while processing the login
   */
  @Override
  @LogMonitoringInvocation
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws IOException {

    AuthResponse response = authService.login(loginRequest);

    String refreshToken = response.getRefreshToken();

    ResponseCookie cookie = ResponseCookie.from("refreshToken",refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .sameSite("Strict")
        .maxAge(response.getMaxAgeSeconds())
        .build();

    response.setRefreshToken(null);

    log.info("로그인 성공 - 학번: {}, 신규유저: {}",
        response.getStudentNumber(), response.isNewUser());

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response);
  }

  /**
   * Reissues access and refresh tokens using the refresh token provided in the request cookie.
   *
   * @param refreshToken the refresh token read from the `refreshToken` cookie
   * @return a TokenReissueResponse containing the newly issued access token and related metadata; the new refresh token is set in a `Set-Cookie` header and removed from the response body
   */
  @Override
  @LogMonitoringInvocation
  @PostMapping("/issue")
  public ResponseEntity<TokenReissueResponse> issueAccessToken(
      @CookieValue(name="refreshToken") String refreshToken) {

    TokenReissueResponse response = authService.reissueToken(refreshToken);

    ResponseCookie cookie = ResponseCookie.from("refreshToken",response.getRefreshToken())
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .maxAge(response.getMaxAge())
        .build();

    response.setRefreshToken(null);

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response);
  }
}