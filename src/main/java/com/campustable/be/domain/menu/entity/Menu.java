package com.campustable.be.domain.menu.entity;

import com.campustable.be.domain.category.entity.Category;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

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

  public void update(MenuUpdateRequest request) {

    if(request.getMenuName() != null &&  !request.getMenuName().isEmpty()) {
      this.menuName = request.getMenuName();
    }
    if (request.getPrice() != null) {
      this.price=request.getPrice();
    }
    if (request.getMenuUrl() != null && !request.getMenuUrl().isBlank()) {
      this.menuUrl = request.getMenuUrl();
    }
    if (request.getAvailable() != null) {
      this.available = request.getAvailable();
    }
    if (request.getStockQuantity() != null) {
      this.stockQuantity = request.getStockQuantity();
    }
  }

  public void decreaseStockQuantity(int quantity) {
    if(this.stockQuantity - quantity < 0) {
      throw new CustomException(ErrorCode.MENU_OUT_OF_STOCK);
    }
    else if(this.stockQuantity - quantity == 0) {
      this.available = false;
    }
    this.stockQuantity -= quantity;
  }

}
