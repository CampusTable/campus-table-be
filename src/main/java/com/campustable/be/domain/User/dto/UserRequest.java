package com.campustable.be.domain.User.dto;

import com.campustable.be.domain.User.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
  private String loginId;
  private String password;
  private String role;

  public static User toEntity(UserRequest request){
    return User.builder()
        .loginId(request.getLoginId())
        .password(request.getPassword()) //향후에 암호화해서 저장할예정
        .role(request.getRole())
        .build();
  }
}
