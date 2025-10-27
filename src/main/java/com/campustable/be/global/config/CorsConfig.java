package com.campustable.be.global.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  private static final long CORS_MAX_AGE = 3600L;

  /**
   * 허용된 CORS Origin 목록 (고정 도메인 - 정확한 매칭)
   */
  private static final List<String> ALLOWED_ORIGINS = List.of(

      // 3000번 포트
      "https://www.campustable.shop", // 프론트 배포
      "http://43.201.107.56:3003",

      // API
      "https://api.campustable.shop", // 메인 API 서버
      "http://43.201.107.56:8087",
      "https://api.test.campustable.shop", // 테스트 API 서버
      "http://43.201.107.56:8088",

      // Local
      "http://localhost:8080", // 로컬 API 서버
      "http://localhost:3000", // 로컬 웹 서버
      "https://localhost:3000",
      "http://localhost:3001", //

      "http://10.*:*", // 10.0.0.0/8
      "http://172.*:*", // 172.16.0.0/12 전체 (16~31)
      "http://192.168.*:*" // 192.168.0.0/16
  );

  /**
   * CORS 허용 HTTP Method
   */
  private static final List<String> ALLOWED_METHODS = List.of(
      "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
  );

  /**
   * CORS 허용 Headers
   */
  private static final List<String> ALLOWED_HEADERS = List.of("*");

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(ALLOWED_ORIGINS);
    configuration.setAllowedMethods(ALLOWED_METHODS);
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(ALLOWED_HEADERS);
    configuration.setMaxAge(CORS_MAX_AGE);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
