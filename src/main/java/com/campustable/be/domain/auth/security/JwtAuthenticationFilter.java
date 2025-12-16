package com.campustable.be.domain.auth.security;

import com.campustable.be.domain.auth.provider.JwtProvider;
import com.campustable.be.domain.auth.service.CustomUserDetailService;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
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
  private static final List<String> EXCLUSION_URL_PATTERNS = List.of(
      "/api/auth",
      "/swagger-ui",
      "/v3/api-docs",
      "/docs/swagger-ui"
  );

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain) throws ServletException, IOException {

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
        writeErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
        return ;
      } catch (JwtException e) {
        writeErrorResponse(response, ErrorCode.JWT_INVALID);
        return ;
      } catch(CustomException e){
        writeErrorResponse(response, e.getErrorCode());
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

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {

    String path = new UrlPathHelper().getRequestUri(request);

    return EXCLUSION_URL_PATTERNS.stream()
        .anyMatch(path::startsWith);
  }

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

