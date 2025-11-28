package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OperatingHoursControllerDocs {

  @Operation(
      summary = "식당 운영시간 생성",
      description = """
            ### 요청 파라미터
            - `id` (PathVariable, Long, required): 운영시간을 등록할 식당의 고유 ID
            - Request Body(JSON):
                - `dayOfWeek` (DayOfWeekEnum, required): 요일 (예: MON, TUE, WED...)
                - `openTime` (String, required): 오픈 시간 (HH:mm 형식)
                - `closeTime` (String, required): 마감 시간 (HH:mm 형식)
                - `breaksStartTime` (String, optional): 브레이크타임 시작 시간 (HH:mm)
                - `breaksCloseTime` (String, optional): 브레이크타임 종료 시간 (HH:mm)

            ### 응답 데이터
            - `operatingHoursId` (Long): 생성된 운영시간 ID
            - `cafeteriaId` (Long): 식당 ID
            - `dayOfWeek` (DayOfWeekEnum): 요일
            - `openTime` (String): 오픈 시간 (HH:mm)
            - `closeTime` (String): 마감 시간 (HH:mm)
            - `breaksStartTime` (String): 브레이크타임 시작 시간 (HH:mm)
            - `breaksCloseTime` (String): 브레이크타임 종료 시간 (HH:mm)

            ### 사용 방법
            1. 식당 ID를 path에 포함하여 요청을 보냅니다.
            2. 요청 Body에 해당 요일의 운영 시간을 JSON 형태로 전달합니다.
            3. 동일 요일 데이터가 없을 경우 새로운 운영시간 정보가 생성됩니다.
            4. 성공 시 생성된 운영시간 정보가 반환됩니다.

            ### 유의 사항
            - 같은 식당의 동일 요일(dayOfWeek)에 대한 운영시간이 이미 존재하면 생성할 수 없습니다.
            - 시간 값은 반드시 HH:mm 형식이어야 합니다.
            - 존재하지 않는 식당 ID로 요청할 경우 실패합니다.

            ### 예외 처리
            - `CAFETERIA_NOT_FOUND` (404): "요청한 식당 정보를 찾을 수 없습니다."
            - `OPERATING_HOURS_ALREADY_EXISTS` (404): "이미 존재하는 운영시간입니다."
            """
  )
  ResponseEntity<OperatingHoursResponse> createOperatingHoursByCafeteriaId(Long id, OperatingHoursRequest request);


  @Operation(
      summary = "식당 운영시간 조회",
      description = """
            ### 요청 파라미터
            - `id` (PathVariable, Long, required): 운영시간을 조회할 식당의 고유 ID

            ### 응답 데이터
            - 운영시간 목록(List):
                - `operatingHoursId` (Long): 운영시간 ID
                - `cafeteriaId` (Long): 식당 ID
                - `dayOfWeek` (DayOfWeekEnum): 요일
                - `openTime` (String): 오픈 시간 (HH:mm)
                - `closeTime` (String): 마감 시간 (HH:mm)
                - `breaksStartTime` (String): 브레이크타임 시작 시간 (HH:mm)
                - `breaksCloseTime` (String): 브레이크타임 종료 시간 (HH:mm)

            ### 사용 방법
            - 특정 식당의 모든 요일별 운영시간 정보를 조회할 때 사용합니다.
            - 일주일치 운영시간을 화면에 표시하고자 할 때 유용합니다.

            ### 유의 사항
            - 식당 ID가 존재하지 않으면 조회할 수 없습니다.

            ### 예외 처리
            - `CAFETERIA_NOT_FOUND` (404): "요청한 식당 정보를 찾을 수 없습니다."
            """
  )
  ResponseEntity<List<OperatingHoursResponse>> getOperatingHoursByCafeteriaId(Long id);


  @Operation(
      summary = "전체 운영시간 조회",
      description = """
            ### 요청 파라미터
            - 없음

            ### 응답 데이터
            - 운영시간 전체 목록(List):
                - `operatingHoursId` (Long): 운영시간 ID
                - `cafeteriaId` (Long): 식당 ID
                - `dayOfWeek` (DayOfWeekEnum): 요일
                - `openTime` (String): 오픈 시간 (HH:mm)
                - `closeTime` (String): 마감 시간 (HH:mm)
                - `breaksStartTime` (String): 브레이크타임 시작 시간 (HH:mm)
                - `breaksCloseTime` (String): 브레이크타임 종료 시간 (HH:mm)

            ### 사용 방법
            - 모든 식당의 운영시간을 한 번에 조회할 때 사용합니다.
            - 관리자 페이지에서 전체 운영상황을 모니터링할 때 활용됩니다.

            ### 유의 사항
            - 본 API는 조건 필터링 없이 전체 데이터를 반환합니다.
            - 데이터 량이 많을 경우 성능에 영향을 줄 수 있습니다.

            ### 예외 처리
            - 본 API에서는 커스텀 예외가 발생하지 않습니다.
            """
  )
  ResponseEntity<List<OperatingHoursResponse>> getAllOperatingHours();


  @Operation(
      summary = "식당 운영시간 수정",
      description = """
            ### 요청 파라미터
            - `id` (PathVariable, Long, required): 수정할 운영시간의 operatingHoursId
            - Request Body(JSON):
                - `dayOfWeek` (DayOfWeekEnum, required): 요일
                - `openTime` (String, required): 오픈 시간 (HH:mm)
                - `closeTime` (String, required): 마감 시간 (HH:mm)
                - `breaksStartTime` (String, optional): 브레이크타임 시작 시간
                - `breaksCloseTime` (String, optional): 브레이크타임 종료 시간

            ### 응답 데이터
            - `operatingHoursId` (Long): 운영시간 ID
            - `cafeteriaId` (Long): 식당 ID
            - `dayOfWeek` (DayOfWeekEnum): 요일
            - `openTime` (String): 오픈 시간
            - `closeTime` (String): 마감 시간
            - `breaksStartTime` (String): 브레이크타임 시작 시간
            - `breaksCloseTime` (String): 브레이크타임 종료 시간

            ### 사용 방법
            - 기존에 등록된 운영시간 데이터를 수정할 때 사용합니다.
            - 요일 변경 또한 가능합니다.

            ### 유의 사항
            - PathVariable의 id는 ‘운영시간 ID(operatingHoursId)’ 입니다.
            - 존재하지 않는 운영시간 ID로는 수정이 불가능합니다.

            ### 예외 처리
            - `OPERATING_HOURS_NOT_FOUND` (404): "요청한 운영시간 정보를 찾을 수 없습니다."
            """
  )
  ResponseEntity<OperatingHoursResponse> updateOperatingHours(Long id, OperatingHoursRequest request);


  @Operation(
      summary = "식당 운영시간 삭제",
      description = """
            ### 요청 파라미터
            - `id` (PathVariable, Long, required): 삭제할 운영시간의 operatingHoursId

            ### 응답 데이터
            - 204 No Content (Body 없음)

            ### 사용 방법
            - 더 이상 필요하지 않은 운영시간 정보를 삭제할 때 사용합니다.
            - 삭제는 운영시간 ID 기준으로 수행됩니다.

            ### 유의 사항
            - 존재하지 않는 운영시간 ID를 전달하더라도 예외를 발생시키지 않습니다.
              (서비스 로직에서는 경고 로그만 출력하고 종료됩니다.)
            - 실제 삭제가 이루어졌는지 확인하려면 별도 조회가 필요합니다.

            ### 예외 처리
            - 본 API에서는 커스텀 예외가 발생하지 않습니다.
            """
  )
  ResponseEntity<Void> deleteOperatingHours(Long id);

}
