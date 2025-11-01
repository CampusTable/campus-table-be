package com.campustable.be.domain.cafeteria.entity;


import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CafeteriaName {

  STUDENT_HALL("학생회관"),
  JIN_HALL("진관"),
  GUNJA_HALL("군자관");

  private final String name;

  public static CafeteriaName fromCode(String name) {
    try { //Illegal
      return CafeteriaName.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
    }
  }

  public static CafeteriaName fromName(String name) {
    for (CafeteriaName cafeteria : CafeteriaName.values()) {
      if (cafeteria.name.equals(name)) { // displayName 필드 이름에 맞게 수정
        return cafeteria;
      }
    }
    // 유효하지 않은 이름이라면 예외 발생
    throw new CustomException(ErrorCode.INVALID_CAFETERIA_NAME);
    // 새로운 오류 코드(INVALID_CAFETERIA_NAME)가 필요합니다.
  }

}
