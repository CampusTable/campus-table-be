package com.campustable.be.domain.auth.security;

import com.campustable.be.domain.auth.provider.JwtProvider;
import com.campustable.be.domain.auth.service.CustomUserDetailService;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
  private static final List<String> EXCLUSION_URL_PATTERNS = List.of(
      "/api/auth",
      "/swagger-ui",
      "/v3/api-docs",
      "/docs/swagger-ui"
  );

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    try {
      String jwt;
      String bearerToken = request.getHeader("Authorization");
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
        jwt = bearerToken.substring(7); // "Bearer " (7글자) 이후의 토큰 문자열 반환
      } else jwt = null;

      jwtProvider.validateToken(jwt);
      String studentNumber = jwtProvider.getSubject(jwt);
      UserDetails userDetails = userDetailsService.loadUserByUsername(studentNumber);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());

      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    catch (CustomException ex) {
      log.error("jwt필터에서 로깅 토큰에 해당하는 유저가 없거나 토큰이 유효하지않습니다.: {}", ex.getMessage());

      request.setAttribute("exception", ex.getErrorCode().name());
      throw new AuthenticationException(ex.getMessage()){};
    }
    catch (Exception ex) {
       log.error("유효하지않거나 잘못된 토큰 형식입니다.", ex);

    }

    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    // Context Path를 제외한 실제 요청 URI를 얻습니다.
    String path = new UrlPathHelper().getRequestUri(request);

    // 🚨 2. 요청 경로가 예외 목록 중 하나로 시작하는지 검사
    return EXCLUSION_URL_PATTERNS.stream()
        .anyMatch(path::startsWith);
  }
}
