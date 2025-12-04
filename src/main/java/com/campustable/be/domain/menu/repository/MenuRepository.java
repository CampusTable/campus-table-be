package com.campustable.be.domain.menu.repository;

import com.campustable.be.domain.category.entity.Category;
import com.campustable.be.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu,Long>{

    Optional<Menu> findByCategoryAndMenuName(Category category, String menuName);

    List<Menu> findByCategory(Category category);

}
