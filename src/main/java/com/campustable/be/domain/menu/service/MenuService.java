package com.campustable.be.domain.menu.service;


import com.campustable.be.domain.menu.dto.MenuCreateRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.repository.MenuRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<MenuResponse> getAllMenus(){

        List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(MenuResponse::new).toList();
    }

    @Transactional
    public MenuResponse createMenu(MenuCreateRequest requestDto){

        Menu menu = requestDto.toEntity();

        Menu saveMenu = menuRepository.save(menu);

        return new MenuResponse(saveMenu);

    }



}
