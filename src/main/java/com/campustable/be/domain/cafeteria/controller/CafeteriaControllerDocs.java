package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import com.campustable.be.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "식당 관리 (Cafeteria)", description = "교내 식당 정보를 관리하고 조회하는 API")
public interface CafeteriaControllerDocs {

  String CODE_DESCRIPTION = "식당 고유 식별 코드 (Enum Key). STUDENT_HALL, JINGWAN, GUNJA_HALL 중 하나. 대소문자 구분 없음.";
  String CAFETERIA_NOT_FOUND_DESC = "CAFETERIA_NOT_FOUND: 요청된 코드의 식당 정보가 DB에 존재하지 않습니다.";

  // --- 1. 식당 생성 (POST) ---
  @Operation(
      summary = "1. 식당 생성",
      description = "특정 이름(Enum에 정의된 Value)을 가진 새로운 식당 정보를 시스템에 등록합니다.",
      requestBody = @RequestBody(
          description = "생성할 식당 정보 (name은 Enum Value와 일치해야 함)",
          content = @Content(
              schema = @Schema(implementation = CafeteriaRequest.class),
              examples = @ExampleObject(
                  name = "생성 요청 예시",
                  value = "{\"name\": \"학생회관\", \"description\": \"학생회관 1층에 위치한 주 식당입니다.\", \"address\": \"서울특별시 광진구 능동로 209 학생회관 1층\"}"
              )
          )
      ),
      responses = {
          @ApiResponse(responseCode = "201", description = "성공적인 식당 생성",
              content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))),
          @ApiResponse(responseCode = "409", description = "이미 존재하는 식당이거나 유효하지 않은 이름",
              content = @Content(schema = @Schema(implementation = CustomException.class),
                  examples = {
                      @ExampleObject(name = "이미 존재 오류", value = "{\"code\": \"CAFETERIA_ALREADY_EXISTS\", \"message\": \"이미 동일한 이름의 식당이 존재합니다.\"}"),
                      @ExampleObject(name = "이름 불일치 오류", value = "{\"code\": \"INVALID_CAFETERIA_NAME\", \"message\": \"요청된 식당 이름이 유효하지 않습니다.\"}"),
                  }
              ))
      }
  )
  ResponseEntity<CafeteriaResponse> createCafeteria(CafeteriaRequest request);


  // --- 2. 식당 조회 (GET) ---
  @Operation(
      summary = "2. 식당 조회",
      description = "특정 식별 코드를 가진 식당의 상세 정보를 조회합니다.",
      parameters = @Parameter(name = "code", in = ParameterIn.PATH, description = CODE_DESCRIPTION, required = true, example = "STUDENT_HALL"),
      responses = {
          @ApiResponse(responseCode = "200", description = "성공적인 조회",
              content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))),
          // 404 응답 추가 (DB에 해당 코드가 없을 경우)
          @ApiResponse(responseCode = "404", description = CAFETERIA_NOT_FOUND_DESC,
              content = @Content(schema = @Schema(implementation = CustomException.class)))
      }
  )
  ResponseEntity<CafeteriaResponse> getCafeteriaByCode(String code);


  // --- 3. 식당 수정 (PATCH) ---
  @Operation(
      summary = "3. 식당 수정 (부분 업데이트)",
      description = "특정 식별 코드를 가진 식당의 정보를 부분적으로 수정합니다. Request Body에는 수정할 필드만 포함합니다.",
      parameters = @Parameter(name = "code", in = ParameterIn.PATH, description = CODE_DESCRIPTION, required = true, example = "GUNJA_HALL"),
      requestBody = @RequestBody(
          description = "수정할 식당 정보 (부분 업데이트 가능)",
          content = @Content(
              schema = @Schema(implementation = CafeteriaRequest.class),
              examples = @ExampleObject(
                  name = "부분 수정 예시",
                  value = "{\"description\": \"군자관 식당은 리모델링을 완료했습니다.\"}"
              )
          )
      ),
      responses = {
          @ApiResponse(responseCode = "200", description = "성공적인 수정",
              content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))),
          @ApiResponse(responseCode = "404", description = CAFETERIA_NOT_FOUND_DESC,
              content = @Content(schema = @Schema(implementation = CustomException.class)))
      }
  )
  ResponseEntity<CafeteriaResponse> updateCafeteria(String code, CafeteriaRequest request);


  // --- 4. 식당 삭제 (DELETE) ---
  @Operation(
      summary = "4. 식당 삭제",
      description = "특정 식별 코드를 가진 식당 정보를 삭제합니다.",
      parameters = @Parameter(name = "code", in = ParameterIn.PATH, description = CODE_DESCRIPTION, required = true, example = "JINGWAN"),
      responses = {
          @ApiResponse(responseCode = "204", description = "성공적인 삭제 (No Content)"),
          @ApiResponse(responseCode = "404", description = CAFETERIA_NOT_FOUND_DESC)
      }
  )
  ResponseEntity<Void> deleteCafeteria(String code);
}
