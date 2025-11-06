package com.campustable.be.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MenuUpdateRequest {

    private String menuName;
    private Integer price;
    private String menuPicture;
    private Boolean available;
    private Integer stockQuantity;
//    private Long categoryId;

}
