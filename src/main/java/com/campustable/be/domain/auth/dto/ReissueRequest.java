package com.campustable.be.domain.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class ReissueRequest {
  private String refreshToken;


}
