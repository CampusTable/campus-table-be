package com.campustable.be.domain.menu.controller;

import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.domain.menu.service.MenuService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController implements MenuControllerDocs {

    private final MenuService menuService;


    @Override
    @GetMapping
    @LogMonitoringInvocation
    public ResponseEntity<List<MenuResponse>> getAllMenus(){

        List<MenuResponse> menus = menuService.getAllMenus();

        return ResponseEntity.ok(menus);

    }

    @Override
    @LogMonitoringInvocation
    @GetMapping("/category/{category-id}")
    public ResponseEntity<List<MenuResponse>> getAllMenusByCategoryId(
           @PathVariable(name = "category-id") Long categoryId){

        List<MenuResponse> menus = menuService.getAllMenusByCategory(categoryId);

        return  ResponseEntity.ok(menus);

    }

    @Override
    @PostMapping
    @LogMonitoringInvocation
    public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest createRequest){
        MenuResponse createMenu = menuService.createMenu(createRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createMenu);
    }

    @Override
    @PatchMapping("/{menu-id}")
    @LogMonitoringInvocation
    public ResponseEntity<MenuResponse> updateMenu(
            @PathVariable(name = "menu-id") Long menuId,
            @RequestBody MenuUpdateRequest updateRequest){

        MenuResponse updateMenu = menuService.updateMenu(menuId, updateRequest);

        return ResponseEntity.ok(updateMenu);
    }

    @Override
    @LogMonitoringInvocation
    @DeleteMapping("/{menu-id}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable(name = "menu-id") Long menuId) {

        menuService.deleteMenu(menuId);

        return ResponseEntity.noContent().build();
    }

}
