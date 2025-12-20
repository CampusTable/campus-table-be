package com.campustable.be.domain.cart.controller;


import com.campustable.be.domain.cart.dto.CartRequest;
import com.campustable.be.domain.cart.dto.CartResponse;
import com.campustable.be.domain.cart.service.CartService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController implements CartControllerDocs{

  private final CartService cartService;

  @LogMonitoringInvocation
  @Override
  @PostMapping("/items")
  public ResponseEntity<CartResponse> addOrUpdateItems(@RequestBody CartRequest request) {
    CartResponse response = cartService.updateCartItem(request.getMenu_id(), request.getQuantity());

    return ResponseEntity.ok(response);
  }

  @LogMonitoringInvocation
  @Override
  @PostMapping("/")
  public ResponseEntity<CartResponse> getCart(){

    CartResponse response = cartService.getCart();

    return ResponseEntity.ok(response);
  }


  @LogMonitoringInvocation
  @Override
  @DeleteMapping("/{cartId}")
  public void deleteCart(@PathVariable Long cartId) {
    cartService.deleteCart(cartId);
  }

  @LogMonitoringInvocation
  @Override
  @DeleteMapping("/cart/items/{cartItemId}")
  public void deleteCartItem(@PathVariable Long cartItemId) {
    cartService.deleteCartItem(cartItemId);
  }
}
