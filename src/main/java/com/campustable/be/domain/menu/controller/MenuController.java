package com.campustable.be.domain.menu.controller;

import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.domain.menu.dto.TopMenuResponse;
import com.campustable.be.domain.menu.service.MenuService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController implements MenuControllerDocs {


  private final MenuService menuService;


    @Override
    @GetMapping("/menus")
    @LogMonitoringInvocation
    public ResponseEntity<List<MenuResponse>> getAllMenus(){

    List<MenuResponse> menus = menuService.getAllMenus();

    return ResponseEntity.ok(menus);

  }

    @Override
    @LogMonitoringInvocation
    @GetMapping("/category/{category_id}/menus")
    public ResponseEntity<List<MenuResponse>> getAllMenusByCategoryId(
           @PathVariable(name = "category_id") Long categoryId){

    List<MenuResponse> menus = menuService.getAllMenusByCategory(categoryId);

    return ResponseEntity.ok(menus);

  }



  @Override
  @LogMonitoringInvocation
  @GetMapping("/cafeteria/{cafeteria-id}")
  public ResponseEntity<List<MenuResponse>> getAllMenusByCafeteriaId(
      @PathVariable(name = "cafeteria-id") Long cafeteriaId
  ) {
    return ResponseEntity.ok(menuService.getAllMenusByCafeteriaId(cafeteriaId));
  }

    @Override
    @PostMapping(value = "/admin/menus", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    @LogMonitoringInvocation
    public ResponseEntity<MenuResponse> createMenu(
        @Valid @ModelAttribute MenuRequest request
    ){
        MenuResponse createMenu = menuService.createMenu(request, request.getImage());

    return ResponseEntity.status(HttpStatus.CREATED).body(createMenu);
  }

    @Override
    @PostMapping(value = "/admin/menus/{menu_id}/image" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @LogMonitoringInvocation
    public ResponseEntity<MenuResponse> uploadMenuImage(
        @PathVariable(name = "menu_id") Long menuId,
        @RequestParam("image") MultipartFile image){

      MenuResponse response = menuService.uploadMenuImage(menuId, image);

      return ResponseEntity.ok(response);

    }

    @Override
    @PatchMapping("/admin/menus/{menu_id}")
    @LogMonitoringInvocation
    public ResponseEntity<MenuResponse> updateMenu(
            @PathVariable(name = "menu_id") Long menuId,
            @RequestBody MenuUpdateRequest updateRequest){

    MenuResponse updateMenu = menuService.updateMenu(menuId, updateRequest);

    return ResponseEntity.ok(updateMenu);
  }

    @Override
    @LogMonitoringInvocation
    @DeleteMapping("/admin/menus/{menu_id}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable(name = "menu_id") Long menuId) {

    menuService.deleteMenu(menuId);

    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/cafeteria/{cafeteria-id}/top-menus")
  @LogMonitoringInvocation
  public ResponseEntity<List<TopMenuResponse>> getTop3MenusByCafeteriaId(@PathVariable(name = "cafeteria-id") Long cafeteriaId) {

    List<TopMenuResponse> topMenus = menuService.getTop3MenusByCafeteriaId(cafeteriaId);

    return ResponseEntity.ok(topMenus);
  }

}
