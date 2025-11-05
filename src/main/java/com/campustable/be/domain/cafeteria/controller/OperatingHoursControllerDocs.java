package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "운영시간 API", description = "식당 운영시간 관리 및 조회 관련 API")
public interface OperatingHoursControllerDocs {

  @Operation(
      summary = "식당 운영시간 등록",
      description = """
          ### 요청 파라미터
          - `id` (PathVariable, Long, required): 운영시간을 등록할 식당의 ID
          - `dayOfWeek` (DayOfWeekEnum, required): 요일 (예: MON, TUE, WED, THU, FRI, SAT, SUN)
          - `openTime` (LocalTime, required): 식당 오픈 시간 (`HH:mm` 형식)
          - `closeTime` (LocalTime, required): 식당 마감 시간 (`HH:mm` 형식)
          - `breaksStartTime` (LocalTime, optional): 브레이크타임 시작 시간 (`HH:mm` 형식)
          - `breaksCloseTime` (LocalTime, optional): 브레이크타임 종료 시간 (`HH:mm` 형식)

          ### 응답 데이터
          - `operatingHoursId` (Long): 생성된 운영시간 ID
          - `cafeteriaId` (Long): 해당 식당 ID
          - `dayOfWeek` (DayOfWeekEnum): 요일
          - `openTime` (String, HH:mm): 오픈 시간
          - `closeTime` (String, HH:mm): 마감 시간
          - `breaksStartTime` (String, HH:mm): 브레이크타임 시작
          - `breaksCloseTime` (String, HH:mm): 브레이크타임 종료

          ### 사용 방법
          1. 관리자 계정으로 요청해야 합니다. (`/admin` 경로 사용)
          2. PathVariable로 식당 ID를 전달하고, 요청 본문에 JSON 형태로 운영시간 정보를 입력합니다.
          3. 서버는 해당 식당의 운영시간 정보를 등록하고, 생성된 데이터를 반환합니다.

          ### 유의 사항
          - 동일 요일에 중복된 운영시간을 등록하면 예외가 발생할 수 있습니다.
          - `openTime`은 `closeTime`보다 반드시 이전 시간이어야 합니다.
          - `breaksStartTime`, `breaksCloseTime`은 둘 다 입력되어야 브레이크타임이 적용됩니다.

          ### 예외처리
          - `CAFETERIA_NOT_FOUND` (HttpStatus.BAD_REQUEST): 존재하지 않는 식당 ID로 요청한 경우
          - `DUPLICATE_OPERATING_HOURS` (HttpStatus.BAD_REQUEST): 동일 요일의 운영시간이 이미 존재하는 경우
          - `INVALID_TIME_RANGE` (HttpStatus.BAD_REQUEST): 오픈/마감/브레이크타임 시간대가 올바르지 않은 경우
          """
  )
  ResponseEntity<OperatingHoursResponse> createOperatingHoursByCafeteriaId(
      @PathVariable Long id,
      @RequestBody @Valid OperatingHoursRequest request
  );


  @Operation(
      summary = "식당 운영시간 조회",
      description = """
          ### 요청 파라미터
          - `id` (PathVariable, Long, required): 조회할 식당의 ID

          ### 응답 데이터
          - `operatingHoursId` (Long): 운영시간 ID
          - `cafeteriaId` (Long): 식당 ID
          - `dayOfWeek` (DayOfWeekEnum): 요일
          - `openTime` (String, HH:mm): 오픈 시간
          - `closeTime` (String, HH:mm): 마감 시간
          - `breaksStartTime` (String, HH:mm): 브레이크타임 시작 시간
          - `breaksCloseTime` (String, HH:mm): 브레이크타임 종료 시간

          ### 사용 방법
          - 프론트엔드에서 식당 상세 페이지를 렌더링할 때 호출합니다.
          - 반환된 데이터를 통해 요일별 운영시간 정보를 표시합니다.

          ### 유의 사항
          - 존재하지 않는 식당 ID로 요청하면 예외가 발생합니다.
          - 운영시간이 등록되지 않은 경우 빈 데이터 또는 부분 정보만 반환될 수 있습니다.

          ### 예외처리
          - `CAFETERIA_NOT_FOUND` (HttpStatus.BAD_REQUEST): 존재하지 않는 식당 ID
          - `OPERATING_HOURS_NOT_FOUND` (HttpStatus.NOT_FOUND): 해당 식당의 운영시간이 존재하지 않음
          """
  )
  ResponseEntity<OperatingHoursResponse> getOperatingHoursByCafeteriaId(@PathVariable Long id);


  @Operation(
      summary = "전체 운영시간 목록 조회",
      description = """
          ### 요청 파라미터
          - 없음

          ### 응답 데이터
          - `List<OperatingHoursResponse>` 형태로 모든 식당의 운영시간 목록을 반환합니다.
            각 요소는 다음 필드를 포함합니다:
              - `operatingHoursId` (Long)
              - `cafeteriaId` (Long)
              - `dayOfWeek` (DayOfWeekEnum)
              - `openTime` (String, HH:mm)
              - `closeTime` (String, HH:mm)
              - `breaksStartTime` (String, HH:mm)
              - `breaksCloseTime` (String, HH:mm)

          ### 사용 방법
          - 전체 식당의 운영시간 정보를 조회할 때 사용합니다.
          - 관리자 또는 일반 사용자 모두 접근 가능합니다.

          ### 유의 사항
          - 데이터가 많을 경우 성능 저하 가능성이 있으므로, 필요 시 페이징 처리를 권장합니다.
          """
  )
  ResponseEntity<List<OperatingHoursResponse>> getAllOperatingHours();


  @Operation(
      summary = "식당 운영시간 수정",
      description = """
          ### 요청 파라미터
          - `id` (PathVariable, Long, required): 수정할 식당의 ID
          - `dayOfWeek` (DayOfWeekEnum, required): 요일
          - `openTime` (LocalTime, required): 변경할 오픈 시간 (`HH:mm`)
          - `closeTime` (LocalTime, required): 변경할 마감 시간 (`HH:mm`)
          - `breaksStartTime` (LocalTime, optional): 브레이크타임 시작 (`HH:mm`)
          - `breaksCloseTime` (LocalTime, optional): 브레이크타임 종료 (`HH:mm`)

          ### 응답 데이터
          - `operatingHoursId` (Long): 수정된 운영시간 ID
          - `cafeteriaId` (Long): 식당 ID
          - `dayOfWeek` (DayOfWeekEnum): 요일
          - `openTime` (String, HH:mm): 변경된 오픈 시간
          - `closeTime` (String, HH:mm): 변경된 마감 시간
          - `breaksStartTime` (String, HH:mm): 변경된 브레이크타임 시작
          - `breaksCloseTime` (String, HH:mm): 변경된 브레이크타임 종료

          ### 사용 방법
          - 관리자가 식당 운영시간을 수정할 때 사용합니다.
          - 요청 본문에 변경할 값들을 JSON으로 전달합니다.

          ### 유의 사항
          - 존재하지 않는 식당 또는 운영시간은 수정할 수 없습니다.
          - 시간대 형식은 반드시 `HH:mm`이어야 합니다.

          ### 예외처리
          - `CAFETERIA_NOT_FOUND` (HttpStatus.BAD_REQUEST)
          - `OPERATING_HOURS_NOT_FOUND` (HttpStatus.NOT_FOUND)
          - `INVALID_TIME_RANGE` (HttpStatus.BAD_REQUEST)
          """
  )
  ResponseEntity<OperatingHoursResponse> updateOperatingHoursByCafeteriaId(
      @PathVariable Long id,
      @RequestBody @Valid OperatingHoursRequest request
  );


  @Operation(
      summary = "식당 운영시간 삭제",
      description = """
          ### 요청 파라미터
          - `id` (PathVariable, Long, required): 삭제할 식당의 ID

          ### 응답 데이터
          - 없음 (204 No Content)

          ### 사용 방법
          - 관리자가 특정 식당의 운영시간을 삭제할 때 사용합니다.
          - 요청 성공 시 본문이 없는 204 응답을 반환합니다.

          ### 유의 사항
          - 삭제된 후에는 복구가 불가능합니다.

          ### 예외처리
          - `CAFETERIA_NOT_FOUND` (HttpStatus.BAD_REQUEST)
          - `OPERATING_HOURS_NOT_FOUND` (HttpStatus.NOT_FOUND)
          """
  )
  ResponseEntity<Void> deleteOperatingHoursByCafeteriaId(@PathVariable Long id);
}
