package com.campustable.be.domain.menu.service;


import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.exception.MenuAlreadyExistsException;
import com.campustable.be.domain.menu.repository.MenuRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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
    public MenuResponse createMenu(MenuRequest requestDto){

        Optional<Menu> existingMenu = menuRepository.findByCategoryIdAndMenuName(
                requestDto.getCategoryId(),
                requestDto.getMenuName()
        );


        if(existingMenu.isPresent()){
            throw new MenuAlreadyExistsException();
        }

        Menu menu = requestDto.toEntity();

        Menu saveMenu = menuRepository.save(menu);

        return new MenuResponse(saveMenu);

    }



}
