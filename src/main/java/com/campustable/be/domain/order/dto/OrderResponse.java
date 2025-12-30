package com.campustable.be.domain.order.dto;

import com.campustable.be.domain.order.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor // Builder와 함께 사용 시 관례적으로 추가
public class OrderResponse {

  private Long orderId;
  private int totalPrice;
  private LocalDateTime orderDate;
  private List<OrderItemDto> orderItems;

  public static OrderResponse from(Order order) {
    return OrderResponse.builder()
        .orderId(order.getOrderId())
        .totalPrice(order.getTotalPrice())
        .orderDate(order.getCreatedAt()) // BaseTimeEntity의 생성일
        .orderItems(order.getOrderItems().stream()
            .map(OrderItemDto::new)
            .toList())
        .build();
  }
}