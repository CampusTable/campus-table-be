package com.campustable.be.domain.auth.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class ReissueRequest {
  private String refreshToken;


}
