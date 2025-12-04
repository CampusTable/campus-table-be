package com.campustable.be.domain.menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MenuUpdateRequest {

  private String menuName;

  @Min(value = 0,message = "가격은 0원이상이어야 합니다.")
  private Integer price;

  private String menuUrl;

  private Boolean available;
  private Integer stockQuantity;

}
