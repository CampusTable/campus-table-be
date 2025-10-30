package com.campustable.be.domain.menu.service;


import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.exception.MenuAlreadyExistsException;
import com.campustable.be.domain.menu.exception.MenuNotFoundException;
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


    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest requestDto){

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new MenuNotFoundException());

        if(requestDto.getMenuName() != null){
            menu.setMenuName(requestDto.getMenuName());
        }
        if(requestDto.getPrice() != null){
            menu.setPrice(requestDto.getPrice());
        }
        if(requestDto.getMenuPicture() != null){
            menu.setMenuPicture(requestDto.getMenuPicture());
        }
        if(requestDto.getAvailable() != null){
            menu.setAvailable(requestDto.getAvailable());
        }
        if(requestDto.getStockQuantity() != null){
            menu.setStockQuantity(requestDto.getStockQuantity());
        }

        return new MenuResponse(menu);
    }

}
