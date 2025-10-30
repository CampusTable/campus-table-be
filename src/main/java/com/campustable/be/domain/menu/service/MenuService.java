package com.campustable.be.domain.menu.service;


import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.domain.menu.entity.Menu;
import com.campustable.be.domain.menu.exception.MenuAlreadyExistsException;
import com.campustable.be.domain.menu.exception.MenuNotFoundException;
import com.campustable.be.domain.menu.repository.MenuRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;


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

    @Transactional(readOnly = true)
    public List<MenuResponse> getAllMenus(){

        List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(MenuResponse::new).toList();
    }



    @Transactional(readOnly = true)
    public List<MenuResponse> getAllMenusByCategory(Integer categoryId){

        List<Menu> menus = menuRepository.findByCategoryId(categoryId);


        //임시 카테고리 없으면 예외 처리
        if(menus.isEmpty()){
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }

        return  menus.stream().map(MenuResponse::new).toList();

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


    @Transactional
    public void deleteMenu(Long menuId){

        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new MenuNotFoundException());

        menuRepository.deleteById(menuId);
    }


}
