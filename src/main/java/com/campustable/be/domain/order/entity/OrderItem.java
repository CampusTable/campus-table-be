package com.campustable.be.domain.order.entity;


import com.campustable.be.domain.menu.entity.Menu;
import jakarta.persistence.Entity;
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

  private String menuName;
  private int orderPrice;
  private int quantity;

  public static OrderItem createOrderItem(Menu menu, int orderPrice, int quantity) {
    OrderItem orderItem = new OrderItem();
    orderItem.setMenu(menu);
    orderItem.setMenuName(menu.getMenuName());
    orderItem.setOrderPrice(orderPrice);
    orderItem.setQuantity(quantity);
    return orderItem;
  }

  public int getTotalPrice() {
    return getOrderPrice() * getQuantity();
  }

}
