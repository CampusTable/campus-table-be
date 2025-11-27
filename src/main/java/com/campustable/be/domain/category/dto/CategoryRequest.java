package com.campustable.be.domain.category.dto;


import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequest {

  private Long cafeteriaId;

  private String categoryName;

  public Category toEntity(Cafeteria cafeteria) {
    return Category.builder()
        .cafeteria(cafeteria)
        .categoryName(this.categoryName)
        .build();
  }

}
