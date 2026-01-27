package com.campustable.be.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // GLOBAL

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),

  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않거나 형식이 잘못되었습니다."),

  // AUTH

  AUTH_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 확인해 주세요."),

  SSO_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "SSO 인증에 실패했습니다."),

  SCRAPING_STRUCTURE_CHANGED(HttpStatus.INTERNAL_SERVER_ERROR, "대상 웹사이트의 구조가 변경되어 데이터를 가져올 수 없습니다."),

  ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"Access Token이 만료되었습니다. 재발급이 필요합니다."),

  JWT_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다. 다시 로그인해 주세요."),

  REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다. 다시 로그인해 주세요."),

  REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "리프레시토큰이 만료되었습니다. 다시 로그인해 주세요."),
  // MENU
  MENU_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 식당에 이미 존재하는 메뉴입니다."),

  MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),

  INVALID_MENU_NAME(HttpStatus.BAD_REQUEST, "메뉴 이름을 입력해 주세요."),

  INVALID_MENU_PRICE(HttpStatus.BAD_REQUEST, "메뉴 가격은 0원 이상이어야 합니다."),

  INVALID_MENU_AVAILABILITY(HttpStatus.BAD_REQUEST, "메뉴 판매 여부를 입력해주세요"),

  MENU_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "메뉴의 재고가 부족합니다."),

  // Cafeteria

  CAFETERIA_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 식당 정보를 찾을 수 없습니다."),

  CAFETERIA_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 식당 코드입니다."),

  OPERATING_HOURS_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 운영시간 정보를 찾을 수 없습니다."),

  OPERATING_HOURS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 운영시간입니다."),

  //Category

  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),

  CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),

  //User
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

  USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),

  //Cart
  CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을수 없습니다."),

  CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 개별목록을 찾을수 없습니다."),

  CART_ITEM_QUANTITY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "장바구니에는 메뉴당 최대 9개까지만 담을 수 있습니다."),

  CART_MIXED_CAFETERIA(HttpStatus.BAD_REQUEST,"다른 식당의 메뉴는 장바구니에 함께 담을 수 없습니다."),

  //Order
  INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "올바르지 않은 주문 상태 변경입니다."),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),

  //OrderItem
  ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문메뉴를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;
}
