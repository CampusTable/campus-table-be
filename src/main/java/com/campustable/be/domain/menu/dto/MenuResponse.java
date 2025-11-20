package com.campustable.be.domain.menu.dto;


import com.campustable.be.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MenuResponse {

  private Long menuId;
  private Long categoryId;
  private String name;
  private Integer price;
  private String menuUrl;
  private Boolean available;
  private Integer stockQuantity;
  private LocalDateTime createdDate;

  public static MenuResponse from(Menu menu) {
    return new MenuResponse(
        menu.getId(),
        menu.getCategoryId(),
        menu.getMenuName(),
        menu.getPrice(),
        menu.getMenuUrl(),
        menu.getAvailable(),
        menu.getStockQuantity(),
        menu.getCreatedAt()
    );
  }


}
