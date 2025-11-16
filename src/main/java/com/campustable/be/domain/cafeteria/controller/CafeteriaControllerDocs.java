package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface CafeteriaControllerDocs {

  @Operation(
      summary = "식당 등록",
      description = """
            ### 요청 파라미터
            - `name` (String, required): 사용자에게 표시되는 식당 이름 (예: 학생회관, 진관, 군자관)
            - `description` (String, optional): 식당의 간략한 설명
            - `address` (String, optional): 식당의 위치 주소

            ### 응답 데이터
            - `id` (Long): 생성된 식당의 고유 ID
            - `name` (String): 식당 이름
            - `description` (String): 식당 설명
            - `address` (String): 식당 주소

            ### 사용 방법
            1. 관리자 권한이 있는 사용자가 `/api/admin/cafeterias` 엔드포인트에 POST 요청을 보냅니다.
            2. 요청 본문에 `CafeteriaRequest` 형식의 JSON 데이터를 포함합니다.
            3. 서버는 중복 여부를 검사 후 신규 식당 정보를 저장합니다.

            ### 유의 사항
            - `name`은 중복될 수 없습니다.
            - `name` 필드는 반드시 Enum의 DisplayName과 일치해야 합니다.
            - 중복된 이름일 경우 요청이 거절됩니다.

            ### 예외 처리
            - `CAFETERIA_ALREADY_EXISTS` (HttpStatus.BAD_REQUEST, "이름이 중복되는 식당이 이미 존재합니다.")
            """
  )
  ResponseEntity<CafeteriaResponse> createCafeteria(@RequestBody CafeteriaRequest request);


  @Operation(
      summary = "단일 식당 조회",
      description = """
            ### 요청 파라미터
            - `id` (Long, required): 조회할 식당의 고유 ID (PathVariable)

            ### 응답 데이터
            - `id` (Long): 식당의 고유 ID
            - `name` (String): 식당 이름
            - `description` (String): 식당 설명
            - `address` (String): 식당 주소

            ### 사용 방법
            1. 클라이언트는 `/api/cafeterias/{id}`로 GET 요청을 보냅니다.
            2. 서버는 해당 ID에 맞는 식당 정보를 반환합니다.

            ### 유의 사항
            - 존재하지 않는 ID로 요청 시 예외가 발생합니다.

            ### 예외 처리
            - `CAFETERIA_NOT_FOUND` (HttpStatus.BAD_REQUEST, "id에 해당하는 식당이 존재하지 않습니다.")
            """
  )
  ResponseEntity<CafeteriaResponse> getCafeteria(@PathVariable Long id);


  @Operation(
      summary = "전체 식당 목록 조회",
      description = """
            ### 요청 파라미터
            - 없음

            ### 응답 데이터
            - `List<CafeteriaResponse>`: 등록된 모든 식당의 목록
                - `id` (Long): 식당 ID
                - `name` (String): 식당 이름
                - `description` (String): 식당 설명
                - `address` (String): 식당 주소

            ### 사용 방법
            1. 클라이언트는 `/api/cafeterias` 엔드포인트로 GET 요청을 보냅니다.
            2. 서버는 DB에 저장된 모든 식당 정보를 반환합니다.

            ### 유의 사항
            - 등록된 식당이 없을 경우 빈 배열(`[]`)이 반환됩니다.
            - 별도의 인증이 필요하지 않습니다.
            """
  )
  ResponseEntity<List<CafeteriaResponse>> getAllCafeteria();


  @Operation(
      summary = "식당 정보 수정",
      description = """
            ### 요청 파라미터
            - `id` (Long, required, PathVariable): 수정할 식당의 고유 ID
            - `name` (String, optional): 변경할 식당 이름
            - `description` (String, optional): 변경할 설명
            - `address` (String, optional): 변경할 주소

            ### 응답 데이터
            - `id` (Long): 수정된 식당의 ID
            - `name` (String): 수정된 식당 이름
            - `description` (String): 수정된 설명
            - `address` (String): 수정된 주소

            ### 사용 방법
            1. 관리자 권한으로 `/api/admin/cafeterias/{id}`에 PATCH 요청을 보냅니다.
            2. 요청 본문(`CafeteriaRequest`)에는 수정할 필드만 포함하면 됩니다.
            3. 서버는 해당 ID의 식당을 찾아 변경 사항을 반영합니다.

            ### 유의 사항
            - PathVariable의 `id`에 해당하는 식당이 존재하지 않으면 수정이 불가능합니다.
            - 요청 본문에 `id`는 포함하지 않아도 됩니다.

            ### 예외 처리
            - `CAFETERIA_NOT_FOUND` (HttpStatus.BAD_REQUEST, "요청id가 식당테이블에 존재하지 않습니다.")
            """
  )
  ResponseEntity<CafeteriaResponse> updateCafeteria(@RequestBody CafeteriaRequest request, @PathVariable Long id);


  @Operation(
      summary = "식당 삭제",
      description = """
            ### 요청 파라미터
            - `id` (Long, required): 삭제할 식당의 고유 ID (PathVariable)

            ### 응답 데이터
            - 없음 (`204 No Content`)

            ### 사용 방법
            1. 관리자 권한으로 `/api/admin/cafeterias/{id}`에 DELETE 요청을 보냅니다.
            2. 해당 ID의 식당이 존재하면 삭제가 수행됩니다.

            ### 유의 사항
            - 존재하지 않는 ID로 요청 시 삭제가 수행되지 않고 예외가 발생합니다.

            ### 예외 처리
            - `CAFETERIA_NOT_FOUND` (HttpStatus.BAD_REQUEST, "요청id가 식당테이블에 존재하지 않습니다.")
            """
  )
  ResponseEntity<Void> deleteCafeteria(@PathVariable Long id);
}
