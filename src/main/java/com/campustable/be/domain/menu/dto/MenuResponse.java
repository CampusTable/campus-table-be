package com.campustable.be.domain.menu.dto;


import com.campustable.be.domain.menu.entity.Menu;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MenuResponse {

    private Long menuId;
    private Long categoryId;
    private String name;
    private Integer price;
    private String menuUrl;
    private Boolean available;
    private Integer stockQuantity;
    private LocalDateTime createdDate;

    public MenuResponse(Menu menu) {
        this.menuId = menu.getId();
        this.categoryId = menu.getCategoryId();
        this.name = menu.getMenuName();
        this.price = menu.getPrice();
        this.menuUrl = menu.getMenuUrl();
        this.available = menu.getAvailable();
        this.stockQuantity = menu.getStockQuantity();
        this.createdDate = menu.getCreatedAt();

    }


}
