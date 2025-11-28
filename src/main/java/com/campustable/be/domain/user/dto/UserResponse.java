package com.campustable.be.domain.user.dto;


import com.campustable.be.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private String userName;
  private Long userId;
  private String role;

  /**
   * Creates a UserResponse DTO populated from the given User entity.
   *
   * @param user the User entity whose fields will be copied into the response
   * @return a UserResponse containing `userName`, `role`, and `userId` from the provided User
   */
  public static UserResponse from(User user) {
    return UserResponse.builder()
        .userName(user.getUserName())
        .role(user.getRole())
        .userId(user.getUserId())
        .build();
  }
}