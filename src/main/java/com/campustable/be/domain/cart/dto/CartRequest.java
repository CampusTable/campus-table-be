package com.campustable.be.domain.cart.dto;


import lombok.Getter;

@Getter
public class CartRequest {

  private Long menuId;
  private int quantity;
}
