package com.campustable.be.domain.User.dto;


import com.campustable.be.domain.User.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  String userName;
  Long userId;
  String role;

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .userName(user.getUserName())
        .role(user.getRole())
        .userId(user.getUserId())
        .build();
  }
}
