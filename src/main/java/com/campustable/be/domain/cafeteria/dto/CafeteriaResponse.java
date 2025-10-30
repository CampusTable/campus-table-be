package com.campustable.be.domain.cafeteria.dto;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CafeteriaResponse {

  private Long id;

  private String code;

  private String name;

  private String description;

  private String address;

  public static CafeteriaResponse from(Cafeteria cafeteria) {
    return new CafeteriaResponse(
        cafeteria.getCafeteriaId(),
        cafeteria.getCode(),
        cafeteria.getName(),
        cafeteria.getDescription(),
        cafeteria.getAddress()
    );
  }

}
