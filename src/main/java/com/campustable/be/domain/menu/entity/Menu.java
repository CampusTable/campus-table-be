package com.campustable.be.domain.menu.entity;

import com.campustable.be.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "menu_picture", length = 500)
    private String menuPicture;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

}
