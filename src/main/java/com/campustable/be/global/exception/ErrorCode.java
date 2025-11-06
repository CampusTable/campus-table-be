package com.campustable.be.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // GLOBAL

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),

  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),


  // AUTH

  AUTH_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 확인해 주세요."),

  SSO_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "SSO 인증에 실패했습니다."),

  SCRAPING_STRUCTURE_CHANGED(HttpStatus.INTERNAL_SERVER_ERROR, "대상 웹사이트의 구조가 변경되어 데이터를 가져올 수 없습니다."),

  INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"Jwt토큰이 유효기간이 지났거나 올바르지않습니다."),

  REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"리프레시토큰이 없습니다. 로그인을통해 재발급받아주세요"),

  // Cafeteria

  CAFETERIA_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 식당 정보를 찾을 수 없습니다."),

  CAFETERIA_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 식당 코드입니다."),

  //User
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;
}
