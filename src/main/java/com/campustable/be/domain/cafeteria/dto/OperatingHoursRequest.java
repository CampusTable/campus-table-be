package com.campustable.be.domain.cafeteria.dto;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.DayOfWeekEnum;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.domain.cafeteria.service.CafeteriaService;
import com.campustable.be.domain.cafeteria.service.OperatingHoursService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class OperatingHoursRequest {

  @Schema(description = "식당의 비즈니스 고유 코드입니다. (예: STUDENT_HALL, JIN_HALL, GUNJA_HALL)")
  private String cafeteria_code;

  @Schema(description = "요일을 의미합니다. (예: MON, TUE ...")
  private DayOfWeekEnum dayOfWeek;

  @Schema(description = "식당이 오픈하는 시간")
  private LocalTime openTime;

  @Schema(description = "식당이 닫는 시간")
  private LocalTime closeTime;

  @Schema(description = "식당의 브레이크타임 시작시간")
  private LocalTime breaksStartTime;

  @Schema(description = "식당의 브레이크타임 마감시간")
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
