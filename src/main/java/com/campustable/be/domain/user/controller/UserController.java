package com.campustable.be.domain.user.controller;

import com.campustable.be.domain.user.dto.UserRequest;
import com.campustable.be.domain.user.dto.UserResponse;
import com.campustable.be.domain.user.service.UserService;
import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController implements UserControllerDocs{

  private final UserService userService;

  /**
   * Retrieve a list of all user profiles.
   *
   * @return ResponseEntity containing a list of UserResponse instances for all users.
   */
  @Override
  @LogMonitoringInvocation
  @GetMapping("/admin/users")
  public ResponseEntity<List<UserResponse>> getAllUsers(){

    List<UserResponse> users = userService.getAllUsers();

    return ResponseEntity.ok(users);
  }

  /**
   * Retrieves the profile of the currently authenticated user.
   *
   * @return the authenticated user's profile as a {@link UserResponse}
   */
  @LogMonitoringInvocation
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getMyProfile() {
    String userId = SecurityContextHolder.getContext().getAuthentication().getName();

    UserResponse response = userService.getMyUserInfo(Long.valueOf(userId));

    return ResponseEntity.ok(response);
  }

  /**
   * Deletes the currently authenticated user.
   *
   * Deletes the user identified by the authenticated principal in the security context.
   *
   * @return an empty response with HTTP status 204 (No Content)
   */
  @LogMonitoringInvocation
  @DeleteMapping("/users")
  public ResponseEntity<Void> deleteUser() {

    Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

    userService.deleteUser(userId);

    return ResponseEntity.noContent().build();
  }

  /**
   * Delete the user with the given ID.
   *
   * @param userId the ID of the user to delete
   * @return HTTP 204 No Content response
   */
  @LogMonitoringInvocation
  @DeleteMapping("/admin/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Create a new user account and return authentication information.
   *
   * <p>On success, issues the refresh token as a `Set-Cookie` header and removes the token from the
   * returned body.</p>
   *
   * @param userRequest the signup payload containing user credentials and profile data
   * @return the authentication response with the `refreshToken` field cleared; the refresh token is
   *         delivered in a `Set-Cookie` header as an HttpOnly, Secure cookie with SameSite=Strict,
   *         path "/", and max-age taken from the response
   */
  @LogMonitoringInvocation
  @PostMapping  ("/signup")
  public ResponseEntity<AuthResponse> createUser(@RequestBody UserRequest userRequest){
    AuthResponse response = userService.createUser(userRequest);

    String refreshToken = response.getRefreshToken();

    ResponseCookie cookie = ResponseCookie.from("refreshToken",refreshToken)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path("/")
        .maxAge(response.getMaxAgeSeconds())
        .build();
    response.setRefreshToken(null);

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response);
  }

  /**
   * Updates the authenticated user's profile with the provided values.
   *
   * @param userRequest the user fields to update
   * @return the updated user representation
   */
  @LogMonitoringInvocation
  @PatchMapping("/users")
  public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest){
    UserResponse response = userService.updateUser(userRequest);
    return ResponseEntity.ok(response);
  }

}