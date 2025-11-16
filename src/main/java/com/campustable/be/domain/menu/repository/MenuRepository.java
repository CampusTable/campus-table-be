package com.campustable.be.domain.menu.repository;

import com.campustable.be.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu,Long>{

    Optional<Menu> findByCategoryIdAndMenuName(Long categoryId, String menuName);

    List<Menu> findByCategoryId(Long categoryId);

}
