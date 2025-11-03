package com.campustable.be.domain.cafeteria.entity;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CafeteriaCodeEnum {

  JINGWAN("JINGWAN"),
  GUNJAGWAN("GUNJAGWAN"),
  HAKGWAN("HAKGWAN");

  private final String code;

  public static CafeteriaCodeEnum fromName(String name) {
    // stream()을 통해 모든 enum 상수를 순회하며 입력된 name과 일치하는지 확인합니다.
    return Arrays.stream(CafeteriaCodeEnum.values())
        .filter(e -> e.getCode().equalsIgnoreCase(name)) // 대소문자 무시 비교 (선택적)
        .findFirst() // 첫 번째 일치하는 상수를 찾음
        .orElse(null); // 일치하는 상수가 없으면 null 반환
  }
}

