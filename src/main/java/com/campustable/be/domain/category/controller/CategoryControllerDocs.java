package com.campustable.be.domain.category.controller;

import com.campustable.be.domain.category.dto.CategoryRequest;
import com.campustable.be.domain.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryControllerDocs {

  @Operation(
      summary = "카테고리 등록",
      description = """
            ### 요청 파라미터
            - `cafeteriaId` (Long, required): 카테고리가 속할 식당의 ID
            - `categoryName` (String, required): 카테고리 이름

            ### 응답 데이터
            - `categoryId` (Long): 생성된 카테고리의 고유 ID
            - `cafeteriaId` (Long): 식당 ID
            - `categoryName` (String): 카테고리 이름

            ### 사용 방법
            1. 관리자 권한으로 `/api/admin/categories` 엔드포인트에 POST 요청을 보냅니다.
            2. 요청 본문에 `CategoryRequest` (식당ID, 이름)를 포함합니다.
            3. 서버는 해당 식당에 중복된 카테고리가 있는지 확인 후 저장합니다.

            ### 유의 사항
            - 같은 식당 내에서 동일한 `categoryName`은 생성할 수 없습니다.
            - 존재하지 않는 `cafeteriaId`로 요청 시 예외가 발생합니다.

            ### 예외 처리
            - `CAFETERIA_NOT_FOUND` (HttpStatus.NOT_FOUND, "해당 식당을 찾을 수 없습니다.")
            - `CATEGORY_ALREADY_EXISTS` (HttpStatus.CONFLICT, "이미 해당 식당에 존재하는 카테고리입니다.")
            """
  )
  ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request);


  @Operation(
      summary = "식당별 카테고리 조회",
      description = """
            ### 요청 파라미터
            - `cafeteriaId` (Long, required, PathVariable): 조회할 식당의 ID

            ### 응답 데이터
            - `List<CategoryResponse>`: 해당 식당에 등록된 모든 카테고리 목록
                - `categoryId` (Long): 카테고리 ID
                - `categoryName` (String): 카테고리 이름

            ### 사용 방법
            1. 클라이언트는 `/api/cafeterias/{cafeteriaId}/categories`로 GET 요청을 보냅니다.
            2. 서버는 해당 식당에 속한 카테고리 리스트를 반환합니다.

            ### 유의 사항
            - 식당 ID가 유효하지 않으면 조회가 불가능합니다.

            ### 예외 처리
            - `CAFETERIA_NOT_FOUND` (HttpStatus.NOT_FOUND, "해당 식당을 찾을 수 없습니다.")
            """
  )
  ResponseEntity<List<CategoryResponse>> getCategoriesByCafeteriaId(@PathVariable("cafeteriaId") Long cafeteriaId);


  @Operation(
      summary = "전체 카테고리 조회",
      description = """
            ### 요청 파라미터
            - 없음

            ### 응답 데이터
            - `List<CategoryResponse>`: 등록된 모든 카테고리 목록

            ### 사용 방법
            1. `/api/categories` 엔드포인트로 GET 요청을 보냅니다.
            2. 전체 카테고리 목록을 조회할 수 있습니다.
            """
  )
  ResponseEntity<List<CategoryResponse>> getAllCategories();


  @Operation(
      summary = "카테고리 수정 (관리자)",
      description = """
            ### 요청 파라미터
            - `categoryId` (Long, required, PathVariable): 수정할 카테고리의 고유 ID
            - `categoryName` (String, required, Body): 변경할 새로운 카테고리 이름

            ### 응답 데이터
            - `categoryId` (Long): 수정된 카테고리 ID
            - `cafeteriaId` (Long): 식당 ID
            - `categoryName` (String): 변경된 카테고리 이름

            ### 사용 방법
            1. 관리자 권한으로 `/api/admin/categories/{categoryId}`에 PATCH 요청을 보냅니다.
            2. 요청 본문에는 변경할 `categoryName`을 포함합니다. (`cafeteriaId`는 제외 가능)

            ### 유의 사항
            - 변경하려는 이름이 해당 식당 내에 이미 존재하면 수정이 거부됩니다.

            ### 예외 처리
            - `CATEGORY_NOT_FOUND` (HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다.")
            - `CATEGORY_ALREADY_EXISTS` (HttpStatus.CONFLICT, "이미 해당 식당에 존재하는 카테고리입니다.")
            """
  )
  ResponseEntity<CategoryResponse> updateCategory(
      @RequestBody CategoryRequest request,
      @PathVariable("categoryId") Long categoryId
  );


  @Operation(
      summary = "카테고리 삭제 (관리자)",
      description = """
            ### 요청 파라미터
            - `categoryId` (Long, required, PathVariable): 삭제할 카테고리의 고유 ID

            ### 응답 데이터
            - 없음 (`204 No Content`)

            ### 사용 방법
            1. 관리자 권한으로 `/api/admin/categories/{categoryId}`에 DELETE 요청을 보냅니다.
            2. 해당 카테고리가 존재하면 삭제됩니다.

            ### 유의 사항
            - 카테고리 삭제 시, 해당 카테고리에 속한 메뉴들도 함께 삭제될 수 있습니다. (Cascade 설정에 따라 다름)
            - 존재하지 않는 ID로 요청 시 예외가 발생합니다.

            ### 예외 처리
            - `CATEGORY_NOT_FOUND` (HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다.")
            """
  )
  ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId);
}