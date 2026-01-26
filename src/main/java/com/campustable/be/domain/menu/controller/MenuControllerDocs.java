package com.campustable.be.domain.menu.controller;

import com.campustable.be.domain.menu.dto.MenuRequest;
import com.campustable.be.domain.menu.dto.MenuResponse;
import com.campustable.be.domain.menu.dto.MenuUpdateRequest;
import com.campustable.be.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * 메뉴 관리 시스템의 API 명세를 정의하는 인터페이스입니다.
 * 메뉴의 등록, 수정, 삭제 및 다양한 조건별 조회 기능을 제공합니다.
 */
@Tag(name = "Menu Management", description = "메뉴 CRUD 및 조회 API")
public interface MenuControllerDocs {

  /**
   * 시스템에 등록된 모든 메뉴 목록을 조회합니다.
   * * @return 메뉴 정보 리스트를 담은 ResponseEntity
   */
  @Operation(summary = "메뉴 전체 조회", description = "모든 메뉴 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  ResponseEntity<List<MenuResponse>> getAllMenus();

  /**
   * 고유 식별자를 통해 단일 메뉴의 상세 정보를 조회합니다.
   * * @param menuId 조회하고자 하는 메뉴의 ID
   * @return 해당 메뉴의 상세 정보를 담은 ResponseEntity
   */
  @Operation(summary = "단일 메뉴 상세 조회", description = "특정 ID에 해당하는 메뉴의 상세 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<MenuResponse> getMenuById(
      @Parameter(description = "조회할 메뉴 ID", example = "1") Long menuId
  );

  /**
   * 특정 카테고리에 속한 모든 메뉴를 조회합니다.
   * * @param categoryId 카테고리 고유 식별자
   * @return 해당 카테고리의 메뉴 리스트를 담은 ResponseEntity
   */
  @Operation(summary = "카테고리별 메뉴 조회", description = "특정 카테고리 ID에 해당하는 메뉴 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 카테고리를 찾을 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<List<MenuResponse>> getAllMenusByCategoryId(
      @Parameter(description = "조회할 카테고리 ID", example = "1") Long categoryId
  );

  /**
   * 특정 식당에서 제공하는 모든 메뉴를 조회합니다.
   * * @param cafeteriaId 식당 고유 식별자
   * @return 해당 식당의 메뉴 리스트를 담은 ResponseEntity
   */
  @Operation(summary = "식당별 메뉴 조회", description = "식당 ID에 해당하는 메뉴 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 식당을 찾을 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<List<MenuResponse>> getAllMenusByCafeteriaId(
      @Parameter(description = "조회할 식당 ID", example = "1") Long cafeteriaId
  );

  /**
   * 새로운 메뉴를 시스템에 등록합니다. (관리자 권한 필요)
   * * @param request 생성할 메뉴의 상세 정보 DTO + 이미지 파일
   * @return 생성된 메뉴 정보를 담은 ResponseEntity
   */
  @Operation(summary = "신규 메뉴 생성 (관리자 전용)", description = "새로운 메뉴를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "메뉴 생성 성공"),
      @ApiResponse(responseCode = "400", description = "입력값 오류(이름/가격 등)",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "이미 존재하는 메뉴입니다.",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<MenuResponse> createMenu(
      @Parameter(description = "메뉴 정보 및 이미지 파일") MenuRequest request
  );

  /**
   * 메뉴에 이미지를 업로드 합니다.
   * * @param menuId 이미지를 등록할 메뉴의 ID
   * @param image 업로드할 이미지 파일
   * @return 이미지 업로드가 완료된 메뉴 정보를 담은 ResponseEntity
   */
  @Operation(
      summary = "메뉴 이미지 개별 업로드/수정",
      description = "메뉴에 사진을 추가합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이미지 업로드 및 경로 업데이트 성공"),
      @ApiResponse(responseCode = "404", description = "해당 ID의 메뉴를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "유효하지 않은 파일 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<MenuResponse> uploadMenuImage(
      @Parameter(description = "대상 메뉴 ID", example = "1") Long menuId,
      @Parameter(description = "업로드할 이미지 파일") MultipartFile image
  );

  /**
   * 기존 메뉴 정보를 수정합니다. (관리자 권한 필요)
   * * @param menuId 수정할 메뉴의 ID
   * @param menuUpdateRequest 수정할 내용이 담긴 DTO
   * @return 수정 완료된 메뉴 정보를 담은 ResponseEntity
   */
  @Operation(summary = "메뉴 정보 수정 (관리자 전용)", description = "특정 ID의 메뉴 정보를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메뉴 수정 성공"),
      @ApiResponse(responseCode = "400", description = "입력값 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<MenuResponse> updateMenu(
      @Parameter(description = "수정할 메뉴 ID", example = "1") Long menuId,
      MenuUpdateRequest menuUpdateRequest
  );

  /**
   * 특정 메뉴를 시스템에서 삭제합니다. (관리자 권한 필요)
   * * @param menuId 삭제할 메뉴의 ID
   * @return 삭제 성공 시 빈 바디를 담은 ResponseEntity (204 No Content)
   */
  @Operation(summary = "메뉴 삭제 (관리자 전용)", description = "특정 ID의 메뉴를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "메뉴 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<Void> deleteMenu(
      @Parameter(description = "삭제할 메뉴 ID", example = "1") Long menuId
  );
}