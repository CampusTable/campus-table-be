package com.campustable.be.global.config;

import com.campustable.be.domain.auth.security.CustomAccessDeniedHandler;
import com.campustable.be.domain.auth.security.JwtAuthenticationEntryPoint;
import com.campustable.be.domain.auth.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(httpBasic-> httpBasic.disable())
        .formLogin(formLogin -> formLogin.disable())
        .exceptionHandling(handling -> handling
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler))
        .authorizeHttpRequests(auth-> auth
            .requestMatchers(
                "/api/admin/**"
            ).hasRole("ADMIN") //접두사ROLE_주의
            .requestMatchers(
                "/api/auth/**",
                "/v3/api-docs/**",
                "/docs/swagger-ui/**",
                "/swagger-ui.html",
                "/api/signup").permitAll()
            .anyRequest().authenticated());
    http.addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}