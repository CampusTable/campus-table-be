package com.campustable.be.domain.cart.entity;

import com.campustable.be.domain.menu.entity.Menu;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

  public CartItem(Cart cart, Menu menu){
    this.cart = cart;
    this.menu = menu;
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name= "cart_item_id")
  private Long cartItemId;

  @ManyToOne(fetch = FetchType.LAZY)
  private Menu menu;

  @ManyToOne(fetch = FetchType.LAZY)
  private Cart cart;

  private int quantity;




}
