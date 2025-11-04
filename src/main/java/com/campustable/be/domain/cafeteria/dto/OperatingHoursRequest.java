package com.campustable.be.domain.cafeteria.dto;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.DayOfWeekEnum;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class OperatingHoursRequest {

  @Schema(description = "요일을 의미합니다. (예: MON, TUE ...")
  private DayOfWeekEnum dayOfWeek;

  @Schema(description = "식당이 오픈하는 시간")
  @JsonFormat(pattern="HH:mm")
  private LocalTime openTime;

  @Schema(description = "식당이 닫는 시간")
  @JsonFormat(pattern="HH:mm")
  private LocalTime closeTime;

  @Schema(description = "식당의 브레이크타임 시작시간")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksStartTime;

  @Schema(description = "식당의 브레이크타임 마감시간")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksCloseTime;

  public OperatingHours toEntity(Cafeteria cafeteria) {
    return OperatingHours.builder()
        .cafeteria(cafeteria)
        .dayOfWeek(this.dayOfWeek)
        .openTime(this.openTime)
        .closeTime(this.closeTime)
        .breaksCloseTime(this.breaksCloseTime)
        .breaksStartTime(this.breaksStartTime).build();
  }
}
