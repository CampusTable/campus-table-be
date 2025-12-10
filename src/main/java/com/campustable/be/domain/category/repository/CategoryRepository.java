package com.campustable.be.domain.category.repository;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.category.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findByCafeteria(Cafeteria cafeteria);

  Optional<Category> findByCategoryName(String categoryName);

  Optional<Category> findByCafeteriaAndCategoryName(Cafeteria cafeteria, String categoryName);

}
