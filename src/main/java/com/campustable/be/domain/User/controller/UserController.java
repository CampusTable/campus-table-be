package com.campustable.be.domain.User.controller;

import com.campustable.be.domain.User.dto.UserRequest;
import com.campustable.be.domain.User.dto.UserResponse;
import com.campustable.be.domain.User.service.UserService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();

    UserResponse response = userService.getMyUserInfo(userName);

    return ResponseEntity.ok(response);
  }

  @LogMonitoringInvocation
  @DeleteMapping("/users")
  public ResponseEntity<Void> deleteMyProfile() {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    userService.deleteUser(userName);
    return ResponseEntity.noContent().build();
  }

  //토큰도지워야함
  @LogMonitoringInvocation
  @DeleteMapping("/admin/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
    userService.deleteUserById(userId);
    return ResponseEntity.noContent().build();
  }

  @LogMonitoringInvocation
  @PatchMapping ("/users")
  public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
    UserResponse response = userService.createUser(userRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoringInvocation
  @GetMapping("/users")
  public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest){
    UserResponse response = userService.updateUser(userRequest);
    return ResponseEntity.ok(response);
  }


}
