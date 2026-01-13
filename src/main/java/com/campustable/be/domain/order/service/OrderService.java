package com.campustable.be.domain.order.service;

import com.campustable.be.domain.cart.entity.Cart;
import com.campustable.be.domain.cart.repository.CartRepository;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.repository.MenuRepository;
import com.campustable.be.domain.order.dto.OrderResponse;
import com.campustable.be.domain.order.entity.Order;
import com.campustable.be.domain.order.entity.OrderItem;
import com.campustable.be.domain.order.repository.OrderItemRepository;
import com.campustable.be.domain.order.repository.OrderRepository;
import com.campustable.be.domain.user.entity.User;
import com.campustable.be.domain.user.repository.UserRepository;
import com.campustable.be.global.common.SecurityUtil;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final OrderItemRepository orderItemRepository;
  private final StringRedisTemplate stringRedisTemplate;

  public OrderResponse createOrder() {
    Long userId = SecurityUtil.getCurrentUserId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Cart cart = cartRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

    if (cart.getCartItems().isEmpty()) {
      throw new CustomException(ErrorCode.CART_NOT_FOUND);
    }

    List<OrderItem> orderItems = cart.getCartItems().stream()
        .map(cartItem -> {

          Menu menu = cartItem.getMenu();
          menu.decreaseStockQuantity(cartItem.getQuantity());

          return OrderItem.createOrderItem(
              menu,
              menu.getPrice(),
              cartItem.getQuantity()
          );
        })
        .toList();

    Order order = Order.createOrder(user, orderItems);

    orderRepository.save(order);
    user.setCart(null);
    cartRepository.delete(cart);

    // 주문된 메뉴 랭킹 점수 증가
    for(OrderItem orderItem : orderItems) {
      stringRedisTemplate.opsForZSet()
          .incrementScore("menu:rank",String.valueOf(orderItem.getMenu().getId()),orderItem.getQuantity());
    }

    return OrderResponse.from(order);
  }

  public void updateCategoryToReady(Long orderId,Long categoryId) {

    List<OrderItem> items = orderItemRepository.findByOrderOrderIdAndCategoryId(orderId, categoryId);
    if (items.isEmpty()) {
      throw new CustomException(ErrorCode.ORDER_ITEM_NOT_FOUND);
    }

    items.forEach(OrderItem::markAsReady); //PREPARING -> READY
  }

  public void updateCategoryToComplete(Long orderId,Long categoryId) {
    List<OrderItem> items = orderItemRepository.findByOrderOrderIdAndCategoryId(orderId, categoryId);

    if (items.isEmpty()) {
      throw new CustomException(ErrorCode.ORDER_ITEM_NOT_FOUND);
    }

    items.forEach(OrderItem::markAsCompleted);
  }

  public List<OrderResponse> getMyOrders() {
    Long userId = SecurityUtil.getCurrentUserId();

    User user = userRepository.findById(userId).
        orElseThrow(() -> {
          log.error("getMyOrders userId : {}를 db에서 발견하지못했음", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    return orderRepository.findByUserUserIdOrderByCreatedAtDesc(userId).stream()
        .map(OrderResponse::from)
        .toList();
  }

  public List<OrderResponse> getOrdersByUserId(Long userId) {

    User user =  userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return orderRepository.findByUserUserIdOrderByCreatedAtDesc(userId).stream()
        .map(OrderResponse::from)
        .toList();

  }

}
