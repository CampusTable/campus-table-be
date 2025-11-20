package com.campustable.be.domain.menu.entity;

import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "menu")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long id;

  @Column(name = "category_id", nullable = false)
  private Long categoryId;

  @Column(name = "menu_name", nullable = false)
  private String menuName;

  @Column(nullable = false)
  private Integer price;

  @Column(name = "menu_url", length = 500)
  private String menuUrl;

  @Column(name = "available", nullable = false)
  private Boolean available;

  @Column(name = "stock_quantity")
  private Integer stockQuantity;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public void update(MenuUpdateRequest requestDto) {

    if (requestDto.getPrice() != null) {
      this.setPrice(requestDto.getPrice());
    }
    if (requestDto.getMenuUrl() != null && !requestDto.getMenuUrl().isBlank()) {
      this.setMenuUrl(requestDto.getMenuUrl());
    }
    if (requestDto.getAvailable() != null) {
      this.setAvailable(requestDto.getAvailable());
    }
    if (requestDto.getStockQuantity() != null) {
      this.setStockQuantity(requestDto.getStockQuantity());
    }


  }

}
