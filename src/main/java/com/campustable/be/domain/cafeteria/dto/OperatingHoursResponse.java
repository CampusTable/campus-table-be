package com.campustable.be.domain.cafeteria.dto;

import com.campustable.be.domain.cafeteria.entity.DayOfWeekEnum;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OperatingHoursResponse {

  private Long operatingHoursId;

  private Long cafeteriaId;

  private DayOfWeekEnum dayOfWeek;

  @JsonFormat(pattern = "HH:mm") //직렬화 에러해결
  private LocalTime openTime;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime closeTime;

  @Schema(description = "식당의 브레이크타임 시작시간")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksStartTime;

  @Schema(description = "식당의 브레이크타임 마감시간")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksCloseTime;

  public static OperatingHoursResponse from(OperatingHours operatingHours) {
    return new OperatingHoursResponse(
        operatingHours.getOperatingId(),
        operatingHours.getCafeteria().getCafeteriaId(),
        operatingHours.getDayOfWeek(),
        operatingHours.getOpenTime(),
        operatingHours.getCloseTime(),
        operatingHours.getBreaksStartTime(),
        operatingHours.getBreaksCloseTime()
    );
  }

  public OperatingHoursResponse(Long id){
    this.cafeteriaId = id;
  }
}
