package com.campustable.be.domain.order.controller;

import com.campustable.be.domain.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Order API", description = "주문 관련 API (생성, 조회, 상태 변경)")
public interface OrderControllerDocs {

  @Operation(summary = "주문 생성", description = "현재 사용자의 장바구니에 담긴 메뉴들로 주문을 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
      @ApiResponse(responseCode = "400", description = "재고 부족"),
      @ApiResponse(responseCode = "404", description = "장바구니가 비어있거나 사용자를 찾을 수 없음")
  })
  ResponseEntity<OrderResponse> createOrder();

  @Operation(summary = "카테고리별 조리 완료 처리 (관리자)", description = "특정 주문(OrderId) 내의 특정 카테고리(CategoryId) 메뉴들을 모두 '조리 완료'로 변경합니다.")
  ResponseEntity<Void> updateCategoryToReady(
      @Parameter(description = "주문 ID", example = "1") @PathVariable Long orderId,
      @Parameter(description = "카테고리 ID", example = "2") @PathVariable Long categoryId
  );

  @Operation(summary = "카테고리별 수령 완료 처리 (관리자)", description = "특정 주문(OrderId) 내의 특정 카테고리(CategoryId) 메뉴들을 모두 '수령 완료'로 변경합니다.")
  ResponseEntity<Void> updateCategoryToComplete(
      @Parameter(description = "주문 ID", example = "1") @PathVariable Long orderId,
      @Parameter(description = "카테고리 ID", example = "2") @PathVariable Long categoryId
  );

  @Operation(summary = "내 주문 내역 조회", description = "사용자의 주문 내역을 조회합니다. (진행 중인 주문이 상단에 노출됨)")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  ResponseEntity<List<OrderResponse>> getMyOrders();

  @Operation(summary = "특정 유저 주문 조회 (관리자)", description = "관리자가 특정 유저(User ID)의 모든 주문 내역을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 유저를 찾을 수 없음")
  })
  ResponseEntity<List<OrderResponse>> getOrdersByUserId(
      @Parameter(description = "조회할 유저 ID", example = "5")
      @PathVariable Long userId
  );
}