package com.campustable.be.domain.category;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long CategoryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafeteria_id", nullable = false)
  private Cafeteria cafeteriaId;

  @Column(name = "category_name")
  private String categoryName;

}
