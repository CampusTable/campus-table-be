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

  @Column(length = 100, nullable = false)
  String name;

  @Column(length = 100)
  private String description;

  @Column(nullable = false, length = 100)
  private String address;

  public void update(CafeteriaRequest request){

    if (request.getName() != null && !request.getName().isBlank()) {
      this.name = request.getName();
    }

    if (request.getDescription() != null) {
      this.description = request.getDescription();
    }

    if (request.getAddress() != null) {
      this.address = request.getAddress();
    }
  }
}
