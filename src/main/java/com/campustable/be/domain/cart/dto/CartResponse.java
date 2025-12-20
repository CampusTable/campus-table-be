package com.campustable.be.domain.cart.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartResponse {
  private List<CartItemDto> items;
  private int totalPrice;
  private Long cartId;
}
