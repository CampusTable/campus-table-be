package com.campustable.be.domain.auth.security;

import com.campustable.be.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  /**
   * Handles an access-denied event by writing a standardized JSON error response for ACCESS_DENIED.
   *
   * @param request the HTTP servlet request
   * @param response the HTTP servlet response used to write the error payload
   * @param accessDeniedException the exception that triggered this handler
   * @throws IOException if an I/O error occurs while writing the response
   * @throws ServletException if the servlet container encounters an error handling the request
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    writeErrorResponse(response, ErrorCode.ACCESS_DENIED);
  }

  /**
   * Write a standardized JSON error payload to the given HttpServletResponse using the provided ErrorCode.
   *
   * Sets the response status, Content-Type to application/json, character encoding to UTF-8, and writes a JSON
   * object containing `errorCode` and `errorMessage`.
   *
   * @param response the HttpServletResponse to write the error to
   * @param errorCode the ErrorCode whose status and message will be used in the response body
   * @throws IOException if an I/O error occurs while writing the response
   */
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