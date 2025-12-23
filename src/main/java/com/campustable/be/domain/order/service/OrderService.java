package com.campustable.be.domain.order.service;

import com.campustable.be.domain.cart.entity.Cart;
import com.campustable.be.domain.cart.repository.CartRepository;
import com.campustable.be.domain.order.dto.OrderItemDto;
import com.campustable.be.domain.order.dto.OrderResponse;
import com.campustable.be.domain.order.entity.Order;
import com.campustable.be.domain.order.entity.OrderItem;
import com.campustable.be.domain.order.repository.OrderRepository;
import com.campustable.be.domain.user.entity.User;
import com.campustable.be.domain.user.repository.UserRepository;
import com.campustable.be.global.common.SecurityUtil;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
  private final OrderRepository orderRepository;
  private final CartRepository cartRepository;
  private final UserRepository userRepository;

  public OrderResponse createOrder(){
    Long userId = SecurityUtil.getCurrentUserId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Cart cart = cartRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

    if (cart.getCartItems().isEmpty()) {
      throw new CustomException(ErrorCode.CART_NOT_FOUND);
    }

    List<OrderItem> orderItems = cart.getCartItems().stream()
        .map(cartItem-> OrderItem.createOrderItem(
            cartItem.getMenu(),
            cartItem.getMenu().getPrice(),
            cartItem.getQuantity()
        ))
        .toList();

    Order order = Order.createOrder(user, orderItems);

    orderRepository.save(order);
    user.setCart(null);
    cartRepository.delete(cart);

    return OrderResponse.builder()
        .orderId(order.getOrderId())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus())
        .orderDate(order.getCreatedAt())
        .orderItems(order.getOrderItems().stream()
            .map(OrderItemDto::new)
            .toList())
        .build();
  }

  public OrderResponse updateToReady(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    order.markAsReady(); // PREPARING -> READY

    // 변경된 상태가 반영된 Order를 다시 DTO로 변환
    return OrderResponse.builder()
        .orderId(order.getOrderId())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus())
        .orderDate(order.getCreatedAt())
        .orderItems(order.getOrderItems().stream()
            .map(OrderItemDto::new)
            .toList())
        .build();
  }

  public OrderResponse updateToCompleted(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    order.markAsCompleted(); // READY -> COMPLETED

    return OrderResponse.builder()
        .orderId(order.getOrderId())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus())
        .orderDate(order.getCreatedAt())
        .orderItems(order.getOrderItems().stream()
            .map(OrderItemDto::new)
            .toList())
        .build();
  }

  public List<OrderResponse> getMyOrders() {
    Long userId = SecurityUtil.getCurrentUserId();
    return orderRepository.findByUserUserIdOrderByCreatedAtDesc(userId).stream()
        .map(OrderResponse::from)
        .toList();
  }

}
