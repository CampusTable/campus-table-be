package com.campustable.be.domain.menu.service;


import com.campustable.be.domain.category.entity.Category;
import com.campustable.be.domain.category.repository.CategoryRepository;
import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.repository.MenuRepository;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final CategoryRepository categoryRepository;


  @Transactional
  public MenuResponse createMenu(MenuRequest request) {

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> {
          log.warn("createMenu: 유효하지 않은 category id");
          throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    Optional<Menu> existingMenu = menuRepository.findByCategoryAndMenuName(
        category,
        request.getMenuName()
    );

    if (existingMenu.isPresent()) {
      log.error("createMenu: 이미 해당 카테고리에 menu가 존재합니다. 생성이 아닌 수정을 통해 진행해주세요.");
      throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
    }

    Menu menu = request.toEntity(category);
    return MenuResponse.from(menuRepository.save(menu));

  }

  @Transactional(readOnly = true)
  public List<MenuResponse> getAllMenus() {

    return menuRepository.findAll().stream()
        .map(MenuResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<MenuResponse> getAllMenusByCategory(Long categoryId) {

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.warn("getAllMenusByCategory: 유효하지 않은 category id");
          throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    List<Menu> menus = menuRepository.findByCategory(category);

    return menus.stream().map(MenuResponse::from).toList();

  }

  @Transactional(readOnly = true)
  public List<MenuResponse> getAllMenusByCategoryName(String categoryName) {
    Category category = categoryRepository.findByCategoryName(categoryName)
      .orElseThrow(() -> {
        log.error("카테고리 명: {}에 해당하는 Category 객체를 찾을 수 없습니다.", categoryName);
        return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
      });
    List<Menu> menus = menuRepository.findByCategory(category);
    return menus.stream().map(MenuResponse::from).toList();
  }


  @Transactional
  public MenuResponse updateMenu(Long menuId, MenuUpdateRequest request) {

    Optional<Menu> menu = menuRepository.findById(menuId);
    if (menu.isEmpty()) {
      log.error("menuId not found {}", menuId);
      throw new CustomException(ErrorCode.MENU_NOT_FOUND);
    }
    if (request.getMenuName() != null && !request.getMenuName().isBlank() &&
        !request.getMenuName().equals(menu.get().getMenuName())) {

      if (menuRepository.findByCategoryAndMenuName(menu.get().getCategory(), request.getMenuName()).isPresent()) {
        throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
      }
    }

    menu.get().update(request);
    return MenuResponse.from(menuRepository.save(menu.get()));
  }


  @Transactional
  public void deleteMenu(Long menuId) {

    Optional<Menu> menu = menuRepository.findById(menuId);
    if (menu.isEmpty()) {
      log.error("menuId not found {}", menuId);
      throw new CustomException(ErrorCode.MENU_NOT_FOUND);
    } else {
      menuRepository.delete(menu.get());
    }
  }

}

