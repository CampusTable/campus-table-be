package com.campustable.be.domain.menu.dto;

import com.campustable.be.domain.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class MenuRequest {

    private Integer categoryId;
    private String menuName;
    private BigDecimal price;
    private String menuPicture;
    private Boolean available;
    private Integer stockQuantity;


    public Menu toEntity(){
        Menu menu = new Menu();
        menu.setCategoryId(this.categoryId);
        menu.setMenuName(this.menuName);
        menu.setPrice(this.price);
        menu.setMenuPicture(this.menuPicture);
        menu.setAvailable(this.available);
        menu.setStockQuantity(this.stockQuantity);
        return menu;
    }



}
