package com.campustable.be.domain.order.entity;


import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderItemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="order_id")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="menu_id")
  private Menu menu;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private String menuName;
  private int orderPrice;
  private int quantity;

  private Long categoryId;

  public static OrderItem createOrderItem(Menu menu, int orderPrice, int quantity) {
    OrderItem orderItem = new OrderItem();
    orderItem.setMenu(menu);
    orderItem.setStatus(OrderStatus.PREPARING);
    orderItem.setMenuName(menu.getMenuName());
    orderItem.setOrderPrice(orderPrice);
    orderItem.setQuantity(quantity);
    orderItem.setCategoryId(menu.getCategory().getCategoryId());
    return orderItem;
  }

  public int getTotalPrice() {
    return getOrderPrice() * getQuantity();
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
