package com.campustable.be.domain.menu.dto;

import com.campustable.be.domain.category.entity.Category;
import com.campustable.be.domain.menu.entity.Menu;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuRequest {

  @NotNull(message = "카테고리 ID는 필수입니다.")
  private Long categoryId;

  @NotBlank(message = "메뉴 이름은 필수 입니다.")
  private String menuName;

  @NotNull(message = "가격은 필수 입니다.")
  @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
  private Integer price;

  @NotNull(message = "이미지를 위한url은 필수입니다.")
  private String menuUrl;

  @NotNull(message = "판매 가능 여부는 필수입니다.")
  private Boolean available;

  private Integer stockQuantity;


  public Menu toEntity(Category category) {
    return Menu.builder()
        .category(category)
        .menuName(this.getMenuName())
        .price(this.getPrice())
        .menuUrl(this.getMenuUrl())
        .available(this.getAvailable())
        .stockQuantity(this.getStockQuantity())
        .build();
  }


}
