package com.campustable.be.domain.cart.dto;


import lombok.Getter;

@Getter
public class CartRequest {

  private Long menu_id;
  private int quantity;
}
