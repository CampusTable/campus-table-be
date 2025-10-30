package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn; // Parameter 위치 명시를 위한 Import 추가
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Swagger RequestBody 사용을 위한 Import
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // HTTP Method 매핑 Import
import org.springframework.web.bind.annotation.GetMapping; // HTTP Method 매핑 Import
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // HTTP Method 매핑 Import
import org.springframework.web.bind.annotation.PutMapping; // HTTP Method 매핑 Import

// API 그룹 태그 설정
@Tag(name = "01. Cafeteria Management", description = "식당 정보 조회 및 관리 API")
public interface CafeteriaControllerDocs {

  // GET /cafeterias/{code}
  @Operation(
      summary = "식당 상세 정보 조회",
      description = """
          식당의 고유 코드(code)를 사용하여 해당 식당의 상세 정보를 조회합니다.
          
          ### 요청파라미터
          -**`code`** (String): 식당의 비즈니스 고유 코드입니다. (예: HAKGWAN, JINGWAN, GUNJAGWAN)
          
          ### 반환값 
          -**`CafeteriaResponse`**
          - **id** (Long): 식당의 기술적 Primary Key. 관리자 기능(수정/삭제)에 사용될 수 있습니다.
          //... (이하 반환값 상세 생략)
          
          **주의:** 메뉴나 운영 시간 정보는 포함되지 않으며, 별도의 API 호출이 필요합니다.
          """,
      parameters = {
          @Parameter(name = "code", description = "조회할 식당의 고유 코드", example = "HAKGWAN", in = ParameterIn.PATH) // in=PATH 명시
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))),
          @ApiResponse(responseCode = "404", description = "해당 코드의 식당을 찾을 수 없음", content = @Content)
      }
  )
  @GetMapping("cafeterias/{code}")
  public ResponseEntity<CafeteriaResponse> getCafeteriaByCode(@PathVariable String code);

  // POST /admin/cafeterias
  @Operation(
      summary = "식당 생성 (관리자 전용)",
      description = """
        이 API는 **관리자 권한**이 필요합니다. 새로운 식당 정보를 시스템에 등록합니다.
       
        ###  반환값 (HTTP 201 Created)
        //... (반환값 상세 생략)
        
        ** 유의사항:** 요청한 `code`가 이미 존재할 경우, `409 Conflict` 오류가 반환됩니다.
        """,
      requestBody = @RequestBody(
          description = "생성할 식당 정보 (code, name, description, address 포함)",
          required = true,
          content = @Content(schema = @Schema(implementation = CafeteriaRequest.class))
      ),
      responses = {
          @ApiResponse(responseCode = "201", description = "식당 생성 성공", content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))),
          @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
          @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
          @ApiResponse(responseCode = "409", description = "요청한 code가 이미 존재함", content = @Content)
      }
  )
  @PostMapping("/admin/cafeterias") // <-- HTTP 매핑 추가
  public ResponseEntity<CafeteriaResponse> createCafeteria(@org.springframework.web.bind.annotation.RequestBody CafeteriaRequest request);

  // PUT /admin/cafeterias/{code}
  @Operation(
      summary = "식당 정보 수정 (관리자 전용)",
      description = """
        이 API는 **관리자 권한**이 필요하며, 특정 식당의 정보를 수정합니다.
        
        ### 요청파라미터 (Path Variable)
          -**`code`** (String): 식당의 비즈니스 고유 코드입니다. (예: HAKGWAN, JINGWAN, GUNJAGWAN)
        
        ### 요청 본문 (Request Body)
        //... (요청 본문 상세 생략)
        
        **유의사항:** `code` 필드는 **불변(Immutable)**이므로, 요청 본문에 `code`가 포함되더라도 서버 로직은 이를 무시하고 **URL의 `code`만을 식별자**로 사용해 업데이트를 진행합니다.
        
        ###  반환값 (HTTP 200 OK)
        수정이 완료된 식당의 최신 정보 (`CafeteriaResponse`)를 반환합니다.
        
        ###  예상 오류
        - **404 Not Found:** 해당 `code`를 가진 식당이 존재하지 않는 경우.
        """,
      parameters = {
          @Parameter(name = "code", description = "수정할 식당의 고유 코드", example = "HAKGWAN", in = ParameterIn.PATH), // in=PATH 명시
      },
      requestBody = @RequestBody(
          description = "수정할 식당 정보",
          required = true,
          content = @Content(schema = @Schema(implementation = CafeteriaRequest.class))
      ),
      responses = {
          @ApiResponse(responseCode = "200", description = "식당 수정 성공", content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))),
          @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
          @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
          @ApiResponse(responseCode = "404", description = "해당 코드의 식당을 찾을 수 없음", content = @Content)
      }
  )
  @PutMapping("/admin/cafeterias/{code}") // <-- HTTP 매핑 추가 (경로 변수 {code} 사용 가정)
  public ResponseEntity<CafeteriaResponse> updateCafeteria(@PathVariable String code,
      @org.springframework.web.bind.annotation.RequestBody CafeteriaRequest request);


  // DELETE /admin/cafeterias/{code}
  @Operation(
      summary = "식당 삭제 (관리자 전용)",
      description = """
        이 API는 **관리자 권한**이 필요하며, 특정 식당 정보를 영구적으로 삭제합니다.
        
        ### 요청파라미터 (Path Variable)
          -**`code`** (String): 식당의 비즈니스 고유 코드입니다. (예: HAKGWAN, JINGWAN, GUNJAGWAN)
        
        ###  반환값 (HTTP 204 No Content)
        - **성공 시:** **204 No Content** 상태 코드를 반환하며, 본문(Body)은 비어있습니다.
        
        ###  외래 키 제약 조건 (중요)
        - 삭제하려는 식당에 **종속된 운영 시간(`OperatingHours`)이나 메뉴(`Menu`) 데이터가 남아있다면**, DB 제약 조건 위반으로 인해 **`500 Internal Server Error`**가 발생할 수 있습니다. (하위 리소스를 먼저 삭제해야 함)
        
        ###  예상 오류
        - **404 Not Found:** 해당 `code`를 가진 식당이 존재하지 않는 경우.
        - **500 Internal Server Error:** 외래 키 제약 조건 위반 (DB 오류)
        """,
      parameters = {
          @Parameter(name = "code", description = "삭제할 식당의 고유 코드", example = "HAKGWAN", in = ParameterIn.PATH) // in=PATH 명시
      },
      responses = {
          @ApiResponse(responseCode = "204", description = "삭제 성공 (No Content)"),
          @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
          @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
          @ApiResponse(responseCode = "404", description = "해당 코드의 식당을 찾을 수 없음", content = @Content),
          @ApiResponse(responseCode = "500", description = "데이터베이스 외래 키 제약 조건 위반 (하위 리소스 존재)", content = @Content)
      }
  )
  @DeleteMapping("/admin/cafeterias/{code}") // <-- HTTP 매핑 추가 (경로 변수 {code} 사용 가정)
  public ResponseEntity<Void> deleteCafeteria(@PathVariable String code);

}
