package com.campustable.be.domain.cafeteria.dto;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CafeteriaRequest {

  @Schema(description = "사용자에게 표시되는 식당 이름 (예: HAKGWAN, JINGWAN))")
  private String name;

  @Schema(description = "식당의 간략한 설명")
  private String description;

  @Schema(description = "식당의 위치 주소")
  private String address;

  public Cafeteria toEntity(CafeteriaRequest cafeteria) {
    return Cafeteria.builder()
        .name(cafeteria.getName())
        .description(cafeteria.getDescription())
        .address(cafeteria.getAddress())
        .build();
  }

}
