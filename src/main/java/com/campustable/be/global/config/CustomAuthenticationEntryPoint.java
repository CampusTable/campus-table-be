package com.campustable.be.global.config;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    ErrorCode errorCode = ErrorCode.INVALID_JWT_TOKEN;

    String customErrorCodeName = (String)request.getAttribute("exception");

    if (customErrorCodeName != null && customErrorCodeName.equals(ErrorCode.USER_NOT_FOUND.name())) {
      errorCode = ErrorCode.USER_NOT_FOUND;
    }

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
  }

  // 💡 GlobalExceptionHandler의 ErrorResponse DTO와 동일한 구조여야 합니다.
  private record ErrorResponse(String errorCode, String errorMessage) {}
}
