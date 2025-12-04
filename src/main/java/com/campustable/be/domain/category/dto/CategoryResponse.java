package com.campustable.be.domain.category.dto;


import com.campustable.be.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

  private Long categoryId;

  private String categoryName;

  private Long cafeteriaId;

  public static CategoryResponse from(Category category) {
    return new CategoryResponse(
        category.getCategoryId(),
        category.getCategoryName(),
        category.getCafeteria().getCafeteriaId()
    );
  }

}
