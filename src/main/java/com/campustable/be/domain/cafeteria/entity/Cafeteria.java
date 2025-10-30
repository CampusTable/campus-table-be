package com.campustable.be.domain.cafeteria.entity;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cafeterias")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cafeteria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cafeteria_id")
  private Long cafeteriaId;

  @Column(unique = true, nullable = false, length = 20)
  private String code;

  @Column(nullable = false, length = 20)
  private String name;

  @Column(length = 100)
  private String description;

  @Column(nullable = false, length = 100)
  private String address;

  public void update(CafeteriaRequest request){
    this.name = request.getName();
    this.description = request.getDescription();
    this.address = request.getAddress();
  }
}
