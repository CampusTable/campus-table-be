package com.campustable.be.domain.order.entity;

import com.campustable.be.domain.user.entity.User;
import com.campustable.be.global.common.BaseTimeEntity;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="orders")
public class Order extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="order_id")
  private Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  private int totalPrice;

  @Enumerated(EnumType.STRING)
  private OrderStatus status; //


  public static Order createOrder(User user, List<OrderItem> orderItems) {
    Order order = new Order();
    order.setUser(user);
    order.setStatus(OrderStatus.PREPARING);
    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setTotalPrice(orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum());
    return order;
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void markAsReady() {
    if (this.status != OrderStatus.PREPARING) {
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }
    this.status = OrderStatus.READY;
  }

  // 수령 대기 -> 주문 완료
  public void markAsCompleted() {
    if (this.status != OrderStatus.READY) {
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }
    this.status = OrderStatus.COMPLETED;
  }
}