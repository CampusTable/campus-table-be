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

@Tag(name = "Menu Management", description = "메뉴 CURD 및 조회 API")
public interface MenuControllerDocs {

    @Operation(summary = "메뉴 전체 조회", description = "모든 메뉴 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<MenuResponse>> getAllMenus();

    @Operation(summary = "카테고리별 메뉴 조회", description = "특정 카테고리 ID에 해당하는 메뉴 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 카테고리를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<List<MenuResponse>> getAllMenusByCategoryId(
            @Parameter(description = "조회할 카테고리 ID", example = "1")
            Long categoryId
    );

    @Operation(summary = "카테고리 명 메뉴 조회", description = "카테고리 이름에 해당하는 메뉴 목록을 조회합니다.")
    ResponseEntity<List<MenuResponse>> getAllMenusByCategoryName(
      String categoryName
    );

    @Operation(
            summary = "신규 메뉴 생성 (관리자 전용)",
            description = "새로운 메뉴를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "메뉴 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류(이름/가격/사용 가능)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "해당 카테고리에 이미 존재하는 메뉴입니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<MenuResponse> createMenu(MenuRequest menuRequest);

    @Operation(
            summary = "메뉴 정보 수정(관리자 전용)",
            description = "특정 ID의 메뉴 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메뉴 수정 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류" ,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<MenuResponse> updateMenu(
            @Parameter(description = "수정할 메뉴의 ID", example = "1")
            Long menuId,
            MenuUpdateRequest menuUpdateRequest
    );

    @Operation(
            summary = "메뉴 삭제 (관리자 전용)",
            description = "특정 ID의 메뉴를 삭제 합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "메뉴 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없습니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteMenu(
            @Parameter(description = "삭제할 메뉴의 ID", example = "1")
            Long menuId
    );
}
