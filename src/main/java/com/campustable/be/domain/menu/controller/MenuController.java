package com.campustable.be.domain.menu.controller;

import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    private ResponseEntity<List<MenuResponse>> getAllMenus(){

        List<MenuResponse> menus = menuService.getAllMenus();

        return ResponseEntity.ok(menus);

    }

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest createRequest){
        MenuResponse createMenu = menuService.createMenu(createRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createMenu);
    }

    @PatchMapping("/{menuID}")
    public ResponseEntity<MenuResponse> updateMenu(
            @PathVariable Long menuID,
            @RequestBody MenuUpdateRequest updateRequest){

        MenuResponse updateMenu = menuService.updateMenu(menuID, updateRequest);

        return ResponseEntity.ok(updateMenu);
    }

}
