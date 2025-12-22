package com.campustable.be.domain.cart.service;

import com.campustable.be.domain.cart.dto.CartItemDto;
import com.campustable.be.domain.cart.dto.CartResponse;
import com.campustable.be.domain.cart.entity.Cart;
import com.campustable.be.domain.cart.entity.CartItem;
import com.campustable.be.domain.cart.repository.CartItemRepository;
import com.campustable.be.domain.cart.repository.CartRepository;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.repository.MenuRepository;
import com.campustable.be.domain.user.entity.User;
import com.campustable.be.domain.user.repository.UserRepository;
import com.campustable.be.global.common.SecurityUtil;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

  private final CartItemRepository cartItemRepository;
  private final UserRepository userRepository;
  private final MenuRepository menuRepository;
  private final CartRepository cartRepository;

  public CartResponse updateCartItem(Long menuId, int quantity) {

    Long userId = SecurityUtil.getCurrentUserId(); // JWT에서 추출한 유저 ID

    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

    User user = userRepository.findById(userId).
        orElseThrow(() -> {
          log.error("addOrIncreaseCartItem userId : {}를 db에서 발견하지못했음",userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Cart cart = cartRepository.findByUser(user)
        .orElseGet(() -> {
          log.info("사용자의 장바구니가 없어 새로 생성합니다. userId: {}", userId);

          return cartRepository.save(new Cart(user));
        });

    Optional<CartItem> cartItemOpt = cartItemRepository.findByCartAndMenu(cart, menu);

    if (cartItemOpt.isPresent()) {
      if (quantity == 0){
        cartItemRepository.delete(cartItemOpt.get());
      }
      else cartItemOpt.get().setQuantity(quantity);
    } else {
      if (quantity > 0) {
        CartItem newItem = new CartItem(cart, menu);
        newItem.setQuantity(quantity);
        cartItemRepository.save(newItem);
      }
    }

    cartItemRepository.flush();

    List<CartItemDto> cartItems = cartItemRepository.findByCart(cart).stream().
        map(CartItemDto::new).
        toList();

    if (cartItems.isEmpty()) {
      cart.getUser().setCart(null);
      cartRepository.delete(cart);
      return CartResponse.builder()
          .items(List.of())
          .totalPrice(0)
          .build();
    }

    int totalPrice = cartItems.stream()
        .mapToInt(item->item.getPrice() * item.getQuantity())
        .sum();

    return CartResponse.builder().
        items(cartItems)
        .totalPrice(totalPrice)
        .cartId(cart.getCart_id())
        .build();
  }

  public void deleteCartItem(Long cartItemId) {

    Long userId = SecurityUtil.getCurrentUserId(); // JWT에서 추출한 유저 ID

    CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(()-> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

    if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    cartItemRepository.delete(cartItem);

  }

  public void deleteCart(Long cartId) {

    Long userId = SecurityUtil.getCurrentUserId();

    Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

    if (!cart.getUser().getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    cart.getUser().setCart(null);

    cartRepository.delete(cart);
  }

  public CartResponse getCart(){
    Long userId = SecurityUtil.getCurrentUserId(); // JWT에서 추출한 유저 ID

    User user = userRepository.findById(userId).
        orElseThrow(() -> {
          log.error("getCart userId : {}를 db에서 발견하지못했음",userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Optional<Cart> cart = cartRepository.findByUser(user);

    if (cart.isPresent()) {
      List<CartItemDto> cartItems = cartItemRepository.findByCart(cart.get()).stream().
          map(CartItemDto::new).
          toList();

      int totalPrice = cartItems.stream()
          .mapToInt(item->item.getPrice() * item.getQuantity())
          .sum();

      return CartResponse.builder().
          items(cartItems)
          .totalPrice(totalPrice)
          .cartId(cart.get().getCart_id())
          .build();
    }
    else{
      return CartResponse.builder()
          .items(List.of())
          .totalPrice(0)
          .build();
    }


  }
}
