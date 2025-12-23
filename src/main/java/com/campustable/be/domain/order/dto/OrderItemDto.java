package com.campustable.be.domain.order.dto;

import com.campustable.be.domain.order.entity.OrderItem;

public class OrderItemDto {

  private Long menuId;
  private String menuName;
  private int orderPrice;
  private int quantity;
  private int subTotal;
  private String menuUrl;

  public OrderItemDto(OrderItem orderItem) {
    this.menuId = orderItem.getMenu().getId();
    this.menuName = orderItem.getMenu().getMenuName();
    this.orderPrice = orderItem.getOrderPrice(); //주문 당시가격
    this.quantity = orderItem.getQuantity();
    this.subTotal = orderItem.getOrderPrice() * orderItem.getQuantity();
    this.menuUrl = orderItem.getMenu().getMenuUrl();
  }

}
