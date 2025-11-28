package com.campustable.be.domain.category.service;


import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.domain.category.dto.CategoryRequest;
import com.campustable.be.domain.category.dto.CategoryResponse;
import com.campustable.be.domain.category.dto.CategoryUpdateRequest;
import com.campustable.be.domain.category.entity.Category;
import com.campustable.be.domain.category.repository.CategoryRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CafeteriaRepository cafeteriaRepository;

  @Transactional
  public CategoryResponse createCategoryByCafeteriaId(CategoryRequest request) {

    Cafeteria cafeteria = cafeteriaRepository.findById(request.getCafeteriaId())
        .orElseThrow(() -> {
          log.warn("createCategory: 유효하지 않은 cafe id");
          throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    Optional<Category> existCategory = categoryRepository.findByCafeteriaAndCategoryName(cafeteria, request.getCategoryName());

    if (existCategory.isPresent()) {
      log.warn("createCategory: 이미 식당에 Category가 존재합니다. 생성이 아닌 수정을 통해 진행해주세요.");
      throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
    }

    Category category = categoryRepository.save(
        request.toEntity(cafeteria));

    return CategoryResponse.from(category);

  }

  @Transactional(readOnly = true)
  public List<CategoryResponse> getCategoryByCafeteriaId(Long id) {
    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("getCategory: 유효하지 않은 cafe id");
          throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    List<Category> category = categoryRepository.findByCafeteria(cafeteria);

    return category.stream()
        .map(CategoryResponse::from)
        .toList();

  }

  @Transactional(readOnly = true)
  public List<CategoryResponse> getAllCategories() {

    List<Category> category = categoryRepository.findAll();

    return category.stream()
        .map(CategoryResponse::from)
        .toList();
  }

  @Transactional
  public CategoryResponse updateCategory(CategoryUpdateRequest request, Long id) {

    Optional<Category> category = categoryRepository.findById(id);
    if (category.isEmpty()) {
      log.error("categoryId not found {}", id);
      throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    category.get().update(request);
    return CategoryResponse.from(category.get());

  }

  @Transactional
  public void deleteCategory(Long id) {

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("categoryId not found {}", id);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    categoryRepository.delete(category);
  }

}
