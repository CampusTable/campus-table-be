package com.campustable.be.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
  private String sejongPortalId;
  private String sejongPortalPw;
}

