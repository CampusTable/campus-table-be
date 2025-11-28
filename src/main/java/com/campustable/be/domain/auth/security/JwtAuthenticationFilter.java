package com.campustable.be.domain.auth.security;

import com.campustable.be.domain.auth.provider.JwtProvider;
import com.campustable.be.domain.auth.repository.RefreshTokenRepository;
import com.campustable.be.domain.auth.service.CustomUserDetailService;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final CustomUserDetailService userDetailsService;
  private final ObjectMapper objectMapper;
  private final RefreshTokenRepository refreshTokenRepository;
  private static final List<String> EXCLUSION_URL_PATTERNS = List.of(
      "/api/auth",
      "/swagger-ui",
      "/v3/api-docs",
      "/docs/swagger-ui"
  );

  /**
   * Authenticates the incoming request by extracting a Bearer JWT from the Authorization header,
   * validating it, and setting the SecurityContext with the authenticated user; on token-related
   * failures writes a structured JSON error response and short-circuits the filter chain.
   *
   * <p>When a valid access token is present the filter loads corresponding UserDetails and
   * sets a UsernamePasswordAuthenticationToken in SecurityContextHolder. On token expiration the
   * filter attempts to verify a refresh token from cookies and returns specific error codes
   * for access-token-expired, refresh-token-invalid/expired, JWT invalidity, or internal errors.
   *
   * @param request  the HTTP request to inspect for authentication tokens
   * @param response the HTTP response used to write error responses when authentication fails
   * @param chain    the filter chain to continue when authentication succeeds or is not attempted
   * @throws ServletException if an error occurs while invoking the next filter in the chain
   * @throws IOException      if an I/O error occurs while reading the request or writing the response
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String jwt;
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      jwt = bearerToken.substring(7); // "Bearer " (7글자) 이후의 토큰 문자열 반환
    } else jwt = null;

    if (jwt != null) {
      try {
        // 1. Access Token 유효성 검증 (예외 발생 가능 지점)
        jwtProvider.validateToken(jwt);

        Long userId = jwtProvider.getSubject(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (ExpiredJwtException e) {
        String refreshToken = extractRefreshTokenFromCookies(request);
        if (StringUtils.hasText(refreshToken)) {
          try {
            String jti = jwtProvider.getJti(refreshToken);
            if (refreshTokenRepository.findById(jti).isPresent()) {
              writeErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
              return;
            }
          } catch (CustomException | JwtException ex) {
            // 리프레시 토큰 자체가 유효하지 않거나 만료된 경우
            log.warn("Refresh Token 검증 실패: {}", ex.getMessage());
            writeErrorResponse(response, ErrorCode.REFRESH_TOKEN_INVALID);
            return;
          } catch (Exception ex) {
            // Redis 장애 등 예기치 못한 서버 오류
            log.error("Refresh Token 검증 중 서버 에러: {}", ex.getMessage(), ex);
            writeErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
            return;
          }
        }
        writeErrorResponse(response, ErrorCode.REFRESH_TOKEN_EXPIRED);
        return ;
      } catch (CustomException e) {
        writeErrorResponse(response, e.getErrorCode());
        return ;
      }
      catch (JwtException e) {
        writeErrorResponse(response, ErrorCode.JWT_INVALID);
        return ;
      } catch (Exception e) {
        // 6. 그 외 예상치 못한 모든 오류
        log.error("예상치않은 서버의 에러 : {}", e.getMessage(), e);
        writeErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
        return ;
      }
    }

    chain.doFilter(request, response);
  }

  /**
   * Determines whether this filter should be skipped for the given HTTP request.
   *
   * @param request the incoming HTTP servlet request
   * @return `true` if the request URI starts with any configured exclusion pattern, `false` otherwise
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    String path = new UrlPathHelper().getRequestUri(request);

    return EXCLUSION_URL_PATTERNS.stream()
        .anyMatch(path::startsWith);
  }

  /**
   * Retrieves the value of the cookie named "refreshToken" from the request, if present.
   *
   * @param request the HTTP request whose cookies will be searched
   * @return the refresh token string from the "refreshToken" cookie, or {@code null} if not found
   */
  private String extractRefreshTokenFromCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("refreshToken".equals(cookie.getName())) {
          return cookie.getValue(); // Refresh Token 문자열 반환
        }
      }
    }
    return null; // 토큰을 찾지 못한 경우
  }

  /**
   * Writes a JSON error payload to the provided HttpServletResponse based on the given ErrorCode.
   *
   * The response status, content type, and character encoding are set, and a JSON body
   * containing the error code name and message is written.
   *
   * @param response  the servlet response to write the error to
   * @param errorCode the error code whose HTTP status, name, and message populate the response body
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
  private record ErrorResponse(String errorCode, String errorMessage) {}
}
