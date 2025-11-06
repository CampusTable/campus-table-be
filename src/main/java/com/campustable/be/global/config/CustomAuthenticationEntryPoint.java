package com.campustable.be.global.config;

import com.campustable.be.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component // 빈으로 등록되거나 SecurityConfig에서 직접 new로 생성되어야 함
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    // 1. 응답 상태 코드 설정 (401 Unauthorized)
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    // 2. 응답 Content Type 설정 (JSON 및 UTF-8)
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    // 3. 응답 본문 작성 (INVALID_JWT_TOKEN 오류 코드를 JSON으로 변환)
    // JWT 필터에서 CustomException을 던지더라도, AuthenticationEntryPoint는
    // Security가 던진 AuthenticationException을 인자로 받습니다.
    // 여기서는 토큰 문제임을 가정하고 INVALID_JWT_TOKEN 코드를 반환합니다.

    String errorJson = objectMapper.writeValueAsString(
        new ErrorResponse(
            ErrorCode.INVALID_JWT_TOKEN.name(),
            ErrorCode.INVALID_JWT_TOKEN.getMessage()
        )
    );

    response.getWriter().write(errorJson);
  }

  // 💡 참고: JSON 변환을 위해 ErrorResponse DTO가 필요합니다.
  private record ErrorResponse(String errorCode, String errorMessage) {}
}