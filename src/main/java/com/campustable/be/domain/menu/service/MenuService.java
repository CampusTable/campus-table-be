package com.campustable.be.domain.menu.service;


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


  @Transactional
  public MenuResponse createMenu(MenuRequest requestDto) {

    if (requestDto.getMenuName() == null || requestDto.getMenuName().isBlank()) {
      log.error("메뉴 이름을 확인해 주세요");
      throw new CustomException(ErrorCode.INVALID_MENU_NAME);
    }
    if (requestDto.getPrice() == null || requestDto.getPrice() < 0) {
      log.error("가격 값을 확인해 주세요");
      throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
    }
    if (requestDto.getAvailable() == null) {
      log.error("판매 가능 여부를 입력해 주세요");
      throw new CustomException(ErrorCode.INVALID_MENU_AVAILABILITY);
    }
//        if(requestDto.getCategoryId()==null){
//            throw new CustomException(ErrorCode.INVALID_REQUEST);
//        }

    Optional<Menu> existingMenu = menuRepository.findByCategoryIdAndMenuName(
        requestDto.getCategoryId(),
        requestDto.getMenuName()
    );

    if (existingMenu.isPresent()) {
      log.error("메뉴가 해당 식당에 이미 등록되어 있습니다.");
      throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
    }

    Menu menu = requestDto.toEntity();

    Menu saveMenu = menuRepository.save(menu);

    return new MenuResponse(saveMenu);

  }

  @Transactional(readOnly = true)
  public List<MenuResponse> getAllMenus() {

    List<Menu> menus = menuRepository.findAll();

    return menus.stream().map(MenuResponse::new).toList();
  }

  @Transactional(readOnly = true)
  public List<MenuResponse> getAllMenusByCategory(Long categoryId) {

    List<Menu> menus = menuRepository.findByCategoryId(categoryId);

    //임시 카테고리 없으면 예외 처리
    if (menus.isEmpty()) {
      log.error("해당 카테고리id는 없습니다");
      throw new CustomException(ErrorCode.MENU_NOT_FOUND);
    }

    return menus.stream().map(MenuResponse::new).toList();

  }


  @Transactional
  public MenuResponse updateMenu(Long menuId, MenuUpdateRequest requestDto) {

    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

    //예외 처리
    if (requestDto.getMenuName() != null && requestDto.getMenuName().isBlank()) {
      log.error("메뉴 이름을 확인해 주세요");
      throw new CustomException(ErrorCode.INVALID_MENU_NAME);
    }
    if (requestDto.getPrice() != null && requestDto.getPrice() < 0) {
      log.error("가격 값을 확인해 주세요");
      throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
    }

    // null 아닌것들만  수정 + blank 예외
    if (requestDto.getMenuName() != null && !requestDto.getMenuName().isBlank()) {

      // 이미 있는 메뉴이면 예외처리
      Optional<Menu> existingMenu = menuRepository.findByCategoryIdAndMenuName(
          menu.getCategoryId(),
          requestDto.getMenuName());
      if (existingMenu.isPresent()) {
        log.error("메뉴가 해당 식당에 이미 등록되어 있습니다.");
        throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
      }

      //아니면 바꿔주기
      menu.setMenuName(requestDto.getMenuName());
    }
    if (requestDto.getPrice() != null) {
      menu.setPrice(requestDto.getPrice());
    }
    if (requestDto.getMenuUrl() != null && !requestDto.getMenuUrl().isBlank()) {
      menu.setMenuUrl(requestDto.getMenuUrl());
    }
    if (requestDto.getAvailable() != null) {
      menu.setAvailable(requestDto.getAvailable());
    }
    if (requestDto.getStockQuantity() != null) {
      menu.setStockQuantity(requestDto.getStockQuantity());
    }

    return new MenuResponse(menu);
  }


  @Transactional
  public void deleteMenu(Long menuId) {

    Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

    menuRepository.deleteById(menuId);
  }
}
