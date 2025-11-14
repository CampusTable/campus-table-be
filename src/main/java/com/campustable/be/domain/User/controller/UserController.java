package com.campustable.be.domain.User.controller;

import com.campustable.be.domain.User.dto.UserRequest;
import com.campustable.be.domain.User.dto.UserResponse;
import com.campustable.be.domain.User.service.UserService;
import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  @LogMonitoringInvocation
  @GetMapping("/admin/users")
  public ResponseEntity<List<UserResponse>> getAllUsers(){

    List<UserResponse> users = userService.getAllUsers();

    return ResponseEntity.ok(users);
  }

  @LogMonitoringInvocation
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getMyProfile() {
    String userId = SecurityContextHolder.getContext().getAuthentication().getName();

    // Authorities 객체를 가져옵니다.
    Collection<? extends GrantedAuthority> authorities =
        SecurityContextHolder.getContext().getAuthentication().getAuthorities();

// Stream을 사용하여 권한 이름만 추출하고 쉼표와 공백으로 연결합니다.
    String authorityNames = authorities.stream()
        .map(GrantedAuthority::getAuthority) // GrantedAuthority 객체에서 문자열(ROLE_ADMIN)만 추출
        .collect(Collectors.joining(", "));

    log.info("Authorities: {}", authorityNames);

    UserResponse response = userService.getMyUserInfo(Long.valueOf(userId));

    return ResponseEntity.ok(response);
  }

  @LogMonitoringInvocation
  @DeleteMapping("/users")
  public ResponseEntity<Void> deleteUser() {

    Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

    userService.deleteUser(userId);

    return ResponseEntity.noContent().build();
  }

  //토큰도지워야함
  @LogMonitoringInvocation
  @DeleteMapping("/admin/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @LogMonitoringInvocation
  @PostMapping  ("/signup")
  public ResponseEntity<AuthResponse> createUser(@RequestBody UserRequest userRequest){
    AuthResponse response = userService.createUser(userRequest);

    String refreshToken = response.getRefreshToken();

    ResponseCookie cookie = ResponseCookie.from("refreshToken",refreshToken)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .maxAge(response.getMaxAge())
        .build();
    response.setRefreshToken(null);

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response);
  }

  @LogMonitoringInvocation
  @GetMapping("/users")
  public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest){
    UserResponse response = userService.updateUser(userRequest);
    return ResponseEntity.ok(response);
  }

}
