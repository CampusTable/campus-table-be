package com.campustable.be.domain.category.controller;

import com.campustable.be.domain.category.dto.CategoryRequest;
import com.campustable.be.domain.category.dto.CategoryResponse;
import com.campustable.be.domain.category.dto.CategoryUpdateRequest;
import com.campustable.be.domain.category.service.CategoryService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController implements CategoryControllerDocs {

  private final CategoryService categoryService;

  @LogMonitoringInvocation
  @Override
  @PostMapping("/admin/categories")
  public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
    CategoryResponse response = categoryService.createCategoryByCafeteriaId(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoringInvocation
  @GetMapping("/cafeterias/{cafeteriaId}/categories")
  @Override
  public ResponseEntity<List<CategoryResponse>> getCategoriesByCafeteriaId(@PathVariable Long cafeteriaId) {

    return ResponseEntity.ok(categoryService.getCategoryByCafeteriaId(cafeteriaId));
  }

  @LogMonitoringInvocation
  @GetMapping("/categories")
  @Override
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {

    return ResponseEntity.ok(categoryService.getAllCategories());
  }


  @LogMonitoringInvocation
  @PatchMapping("/admin/categories/{categoryId}")
  @Override
  public ResponseEntity<CategoryResponse> updateCategory(
      @Valid @RequestBody CategoryUpdateRequest request,
      @PathVariable Long categoryId){
    CategoryResponse response = categoryService.updateCategory(request, categoryId);
    return ResponseEntity.ok(response);
  }

  @LogMonitoringInvocation
  @DeleteMapping("/admin/categories/{categoryId}")
  @Override
  public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
    categoryService.deleteCategory(categoryId);
    return ResponseEntity.noContent().build();
  }


}
