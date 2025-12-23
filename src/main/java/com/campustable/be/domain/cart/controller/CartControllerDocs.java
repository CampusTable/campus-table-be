package com.campustable.be.domain.cart.controller;

import com.campustable.be.domain.cart.dto.CartRequest;
import com.campustable.be.domain.cart.dto.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CartControllerDocs {

  @Operation(
      summary = "장바구니 메뉴 추가 또는 수량 변경",
      description = """
          ### 요청 파라미터
          - `menu_id` (Long, required): 장바구니에 담을 메뉴 ID
          - `quantity` (int, required): 담을 수량  
            - 1 이상: 해당 수량으로 장바구니에 추가 또는 수정  
            - 0: 해당 메뉴를 장바구니에서 삭제

          ### 응답 데이터
          - `cartId` (Long): 장바구니 ID
          - `items` (List<CartItemDto>): 장바구니에 담긴 메뉴 목록
          - `totalPrice` (int): 장바구니 전체 금액

          ### 특징
          - 수량을 0으로 전달하면 해당 메뉴는 삭제됩니다.
          - **장바구니가 비워지면 장바구니 엔티티가 자동 삭제**됩니다. (이 경우 응답에 `cartId`가 포함되지 않을 수 있습니다.)

          ### 예외 처리
          - `MENU_NOT_FOUND` (404): 메뉴 없음
          - `USER_NOT_FOUND` (404): 유저 없음
          """
  )
  ResponseEntity<CartResponse> addOrUpdateItems(
      @RequestBody CartRequest request
  );

  @Operation(
      summary = "장바구니 조회",
      description = """
          ### 특징
          - JWT 인증 정보로 현재 로그인한 사용자의 장바구니를 조회합니다.
          - 장바구니가 없는 경우 빈 배열(`[]`)과 `totalPrice: 0`을 반환합니다.

          ### 예외 처리
          - `USER_NOT_FOUND` (404): 유저 없음
          - `ACCESS_DENIED` (403): 인증 실패
          """
  )
  ResponseEntity<CartResponse> getCart();

  @Operation(
      summary = "장바구니 전체 삭제",
      description = """
          ### 요청 파라미터
          - `cartId` (Long): 삭제할 장바구니 ID

          ### 상세 로직
          - 해당 장바구니 ID가 현재 로그인한 사용자의 것인지 검증합니다.
          - 사용자와 장바구니의 **연관관계를 명시적으로 끊은 후 삭제**를 진행합니다.
          - 장바구니 삭제 시 하위의 모든 아이템도 함께 삭제됩니다.

          ### 예외 처리
          - `CART_NOT_FOUND` (404): 장바구니 없음
          - `ACCESS_DENIED` (403): **본인의 장바구니가 아닌 경우**
          """
  )
  void deleteCart(@PathVariable Long cartId);

  @Operation(
      summary = "장바구니 특정 메뉴 삭제",
      description = """
          ### 요청 파라미터
          - `cartItemId` (Long): 삭제할 장바구니 아이템 ID

          ### 상세 로직
          1. 해당 `cartItemId`가 현재 로그인한 사용자의 장바구니에 속해 있는지 검증합니다.
          2. 아이템을 삭제한 후, **장바구니에 남은 아이템이 없는지 확인**합니다.
          3. **남은 아이템이 하나도 없다면 장바구니 엔티티도 함께 삭제** 처리합니다.

          ### 예외 처리
          - `CART_ITEM_NOT_FOUND` (404): 아이템 없음
          - `ACCESS_DENIED` (403): **본인의 장바구니 아이템이 아닌 경우**
          """
  )
  void deleteCartItem(@PathVariable Long cartItemId);
}