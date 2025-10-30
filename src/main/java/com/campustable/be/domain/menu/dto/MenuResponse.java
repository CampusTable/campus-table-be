package com.campustable.be.domain.menu.dto;


import com.campustable.be.domain.menu.entity.Menu;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MenuResponse {

    private Long menuId;
    private Integer categoryId;
    private String name;
    private BigDecimal price;
    private String menuPicture;
    private Boolean available;
    private Integer stockQuantity;
    private LocalDateTime createdDate;

    public MenuResponse(Menu menu) {
        this.menuId = menu.getId();
        this.categoryId = menu.getCategoryId();
        this.name = menu.getMenuName();
        this.price = menu.getPrice();
        this.menuPicture = menu.getMenuPicture();
        this.available = menu.getAvailable();
        this.stockQuantity = menu.getStockQuantity();
        this.createdDate = menu.getCreatedAt();

    }


}
