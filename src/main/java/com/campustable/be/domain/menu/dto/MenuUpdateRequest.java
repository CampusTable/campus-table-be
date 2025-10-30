package com.campustable.be.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class MenuUpdateRequest {

    private String menuName;
    private BigDecimal price;
    private String menuPicture;
    private Boolean available;
    private Integer stockQuantity;
    //private Integer categoryId;   카테고리 바꾸는것도 필요한지 몰라서 일단 둡니다.

}
