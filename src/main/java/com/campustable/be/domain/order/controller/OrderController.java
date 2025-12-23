package com.campustable.be.domain.order.controller;


import com.campustable.be.domain.order.dto.OrderResponse;
import com.campustable.be.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder() {
    return ResponseEntity.ok(orderService.createOrder());
  }

  @PatchMapping("/{orderId}/ready")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<OrderResponse> updateToReady(@PathVariable Long orderId) {
    return ResponseEntity.ok(orderService.updateToReady(orderId));
  }

  @PatchMapping("/{orderId}/complete")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<OrderResponse> updateToCompleted(@PathVariable Long orderId) {
    return ResponseEntity.ok(orderService.updateToCompleted(orderId));
  }


}