package com.campustable.be.domain.cafeteria.dto;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CafeteriaResponse {

  @Schema(description ="식당의 고유Id")
  private Long id;

  @Schema(description ="식당의 고유한 이름")
  private String name;

  @Schema(description ="식당의 설명")
  private String description;

  @Schema(description ="식당의 주소")
  private String address;

  public static CafeteriaResponse from(Cafeteria cafeteria) {
    return new CafeteriaResponse(
        cafeteria.getCafeteriaId(),
        cafeteria.getName(),
        cafeteria.getDescription(),
        cafeteria.getAddress()
    );
  }


}
