package com.campustable.be.domain.category.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequest {

  @NotBlank(message = "카테고리 이름은 필수입니다.")
  private String categoryName;

}
