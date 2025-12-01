package com.campustable.be.domain.category.dto;


import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequest {

  @NotNull(message = "식당 ID는 필수입니다.")
  private Long cafeteriaId;

  @NotBlank(message = "카테고리 이름은 필수입니다.")
  private String categoryName;

  public Category toEntity(Cafeteria cafeteria) {
    return Category.builder()
        .cafeteria(cafeteria)
        .categoryName(this.categoryName)
        .build();
  }

}
