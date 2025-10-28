package com.campustable.be.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class SejongMemberInfo {
  private String studentId;
  private String studentName;
}
