package com.campustable.be.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

  PREPARING("조리 중"),

  READY("수령 대기"),

  COMPLETED("주문 완료");

  private final String description;
}
