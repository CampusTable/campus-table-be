package com.campustable.be.domain.menu.service;


import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.service.CafeteriaService;
import com.campustable.be.domain.category.entity.Category;
import com.campustable.be.domain.category.repository.CategoryRepository;
import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.domain.menu.dto.TopMenuResponse;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.repository.MenuRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final CategoryRepository categoryRepository;
  private final CafeteriaService cafeteriaService;
  private final StringRedisTemplate stringRedisTemplate;


  @Transactional
  public MenuResponse createMenu(MenuRequest request) {

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> {
          log.warn("createMenu: 유효하지 않은 category id");
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
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
  public MenuResponse getMenuById(Long menuId) {

    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(()->{
          log.error("getMenuById : 유효하지않은 menuId");
          return new CustomException(ErrorCode.MENU_NOT_FOUND);
        });

    return MenuResponse.from(menu);

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
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    List<Menu> menus = menuRepository.findByCategory(category);

    return menus.stream().map(MenuResponse::from).toList();

  }

  @Transactional(readOnly = true)
  public List<MenuResponse> getAllMenusByCafeteriaId(Long cafeteriaId) {
    // TODO: 임시로 작성한 메서드. 추후 성능 개선 필요
    Cafeteria cafeteria = cafeteriaService.findCafeteriaById(cafeteriaId);
    List<Category> categories = categoryRepository.findByCafeteria(cafeteria);
    List<Menu> menus = new ArrayList<>();
    for (Category category : categories) {
      List<Menu> menusByCategory = menuRepository.findByCategory(category);
      menus.addAll(menusByCategory);
    }

    List<MenuResponse> responses = new ArrayList<>();
    for (Menu menu : menus) {
      responses.add(MenuResponse.from(menu));
    }
    return responses;
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


  @Transactional
  public List<TopMenuResponse> getTopMenusByCafeteriaId(Long cafeteriaId) {

    String key = "cafeteria:"+cafeteriaId+":menu:rank";

    Set<String> topMenus = stringRedisTemplate.opsForZSet().reverseRange(key,0,2);

    if(topMenus == null || topMenus.isEmpty()){
      return List.of();
    }

    List<Long> topMenuIds = topMenus.stream()
        .map(Long::parseLong)
        .toList();

    List<Menu> menus = menuRepository.findAllById(topMenuIds);

    Map<Long,Menu> topMenusMap = menus.stream()
        .collect(Collectors.toMap(Menu::getId, Function.identity()));

    List<TopMenuResponse> topMenusResponse = new ArrayList<>();

    for(int i=0;i<topMenuIds.size();i++){
      Long topMenuId = topMenuIds.get(i);
      Menu menu = topMenusMap.get(topMenuId);

      if(menu != null){
        topMenusResponse.add(TopMenuResponse.of((long)(i+1),menu));
      }
    }

    return topMenusResponse;

  }

}

