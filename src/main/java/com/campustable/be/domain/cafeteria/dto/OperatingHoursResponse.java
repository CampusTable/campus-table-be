package com.campustable.be.domain.cafeteria.dto;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.DayOfWeekEnum;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OperatingHoursResponse {

  private String cafeteriaCode;

  private DayOfWeekEnum dayOfWeek;

  @JsonFormat(pattern = "HH:mm") //직렬화 에러해결
  private LocalTime openTime;
  @JsonFormat(pattern = "HH:mm")
  private LocalTime closeTime;

  public static OperatingHoursResponse from(OperatingHours operatingHours) {
    return new OperatingHoursResponse(
        operatingHours.getCafeteria().getCode(),
        operatingHours.getDayOfWeek(),
        operatingHours.getOpenTime(),
        operatingHours.getCloseTime()
    );
  }
}
