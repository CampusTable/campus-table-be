package com.campustable.be.domain.cart.dto;


import com.campustable.be.domain.cart.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
  private String menuName;
  private int quantity;
  private int price;
  private String menuUrl;
  private Long cartItemId;
  private Long menuId;

  public CartItemDto(CartItem entity) {
    this.menuName = entity.getMenu().getMenuName();
    this.price = entity.getMenu().getPrice();
    this.quantity = entity.getQuantity();
    this.menuUrl =  entity.getMenu().getMenuUrl();
    this.cartItemId = entity.getCartItemId();
    this.menuId = entity.getMenu().getId();
  }
}
