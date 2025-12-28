package com.campustable.be.domain.order.controller;


import com.campustable.be.domain.order.dto.OrderResponse;
import com.campustable.be.domain.order.service.OrderService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController implements OrderControllerDocs {

  private final OrderService orderService;

  @Override
  @LogMonitoringInvocation
  @PostMapping
  public ResponseEntity<OrderResponse> createOrder() {
    return ResponseEntity.ok(orderService.createOrder());
  }

  @Override
  @LogMonitoringInvocation
  @PatchMapping("/{orderId}/categories/{categoryId}/ready")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> updateCategoryToReady(
      @PathVariable Long orderId,
      @PathVariable Long categoryId) {
    orderService.updateCategoryToReady(orderId,categoryId);
    return ResponseEntity.ok().build();
  }

  @Override
  @LogMonitoringInvocation
  @PatchMapping("/{orderId}/categories/{categoryId}/complete")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> updateCategoryToComplete(
      @PathVariable Long orderId,
      @PathVariable Long categoryId
  ) {
    orderService.updateCategoryToComplete(orderId,categoryId);
    return ResponseEntity.ok().build();
  }

  @Override
  @LogMonitoringInvocation
  @GetMapping
  public ResponseEntity<List<OrderResponse>> getMyOrders() {
    return ResponseEntity.ok(orderService.getMyOrders());
  }

  @Override
  @GetMapping("/users/{userId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
  }

}