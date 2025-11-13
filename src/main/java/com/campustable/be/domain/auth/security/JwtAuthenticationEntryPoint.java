package com.campustable.be.domain.auth.security;

import com.campustable.be.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
// Spring Security 설정에 등록하여 인증되지 않은 요청을 처리합니다.
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    log.warn("미인증 요청 감지 (토큰 없음). EntryPoint 실행: {}", authException.getMessage());

    // 토큰이 아예 없는 미인증 상태이므로, '아이디/비밀번호 확인'과 동일한 401 응답으로 처리합니다.
    // 클라이언트는 이 401 응답을 보고 로그인 페이지로 리다이렉션합니다.
    writeErrorResponse(response, ErrorCode.AUTH_FAILED);
  }

  // JWT 필터와 동일한 로직을 사용해 응답 형식을 통일합니다.
  private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

    response.setStatus(errorCode.getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    String errorJson = objectMapper.writeValueAsString(
        new ErrorResponse(
            errorCode.name(),
            errorCode.getMessage()
        )
    );
    response.getWriter().write(errorJson);
    response.getWriter().flush();
  }

  // JWT 필터와 동일한 record 정의를 사용합니다.
  private record ErrorResponse(String errorCode, String errorMessage) {}
}