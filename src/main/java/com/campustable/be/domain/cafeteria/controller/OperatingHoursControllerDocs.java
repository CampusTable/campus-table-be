package com.campustable.be.domain.cafeteria.controller;


import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Cafeteria Management", description = "식당 정보 조회 및 관리 API")
public interface OperatingHoursControllerDocs {

  @Operation(
      summary = "운영 시간 등록 (관리자 전용)",
      description = """
        이 API는 **관리자 권한**이 필요하며, 특정 식당에 새로운 운영 시간 정보를 등록합니다.
        
        ### 식별자 (PathVariable)
        - **`code`** (String): **운영 시간을 등록할 대상 식당**의 비즈니스 고유 코드입니다. (예: HAKGWAN)
        
        ### 요청 본문 (Request Body)
        - **`cafeteriaCode`** (String): 등록 대상 식당 코드 (URL의 code와 일치해야 함).
        - **`dayOfWeek`** (DayOfWeekEnum): 운영 요일 (예: MON, TUE, WED).
        - **`openTime`** (LocalTime): 운영 시작 시간 (HH:mm:ss 형식).
        - **`closeTime`** (LocalTime): 운영 종료 시간 (HH:mm:ss 형식).
        
        ### 반환값 (HTTP 201 Created)
        등록이 완료된 운영 시간 정보 (`OperatingHoursResponse`)를 반환합니다.
        - **operatingId** (Long), **dayOfWeek** (Enum), **openTime** (LocalTime), **closeTime** (LocalTime) 등을 포함합니다.
        
        ### 예상 오류
        - **404 Not Found:** 요청된 `code`를 가진 식당이 존재하지 않는 경우.
        - **409 Conflict:** 해당 식당의 해당 요일에 이미 운영 시간이 등록되어 있는 경우. (중복 체크 로직 구현 시)
        """,
      parameters = {
          @Parameter(
              name = "code",
              description = "운영 시간을 추가할 식당의 비즈니스 코드",
              required = true
          )
      }
  )
  public ResponseEntity<OperatingHoursResponse> createOperatingHours(@PathVariable String code,
      @RequestBody OperatingHoursRequest request);
}
