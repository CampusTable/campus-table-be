package com.campustable.be.global.config;

import com.campustable.be.domain.auth.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final ObjectMapper objectMapper;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(httpBasic-> httpBasic.disable())
        .formLogin(formLogin -> formLogin.disable())
        .exceptionHandling(handling -> handling
            .authenticationEntryPoint(customAuthenticationEntryPoint))
        .authorizeHttpRequests(auth-> auth
            .requestMatchers(
                "/api/auth/**",
                "/v3/api-docs/**",
                "/docs/swagger-ui/**",
                "/swagger-ui.html").permitAll()
            .anyRequest().authenticated());
    http.addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}