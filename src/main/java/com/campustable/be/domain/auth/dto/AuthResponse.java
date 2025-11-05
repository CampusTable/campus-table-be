package com.campustable.be.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {
  String studentNumber;
  String studentName;
  boolean isNewUser;
  String accessToken;
  String refreshToken;
  Long maxAge;
}

