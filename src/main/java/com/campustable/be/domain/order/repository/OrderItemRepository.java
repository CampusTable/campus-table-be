package com.campustable.be.domain.order.repository;

import com.campustable.be.domain.order.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  OrderItemRepository extends JpaRepository<OrderItem, Long> {

  List<OrderItem> findByOrderOrderIdAndCategoryId(Long orderId, Long categoryId);

}
