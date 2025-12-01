package com.campustable.be.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenReissueResponse {
  private String accessToken;
  private String refreshToken;
  private Long maxAge;
}
