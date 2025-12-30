package com.campustable.be.domain.order.repository;

import com.campustable.be.domain.order.entity.Order;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUserUserIdOrderByCreatedAtDesc(Long userId);
}
