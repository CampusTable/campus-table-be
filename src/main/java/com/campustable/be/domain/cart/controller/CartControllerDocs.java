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
            - `cartItemId` (Long): 장바구니 아이템 ID
            - `menuName` (String): 메뉴 이름
            - `quantity` (int): 담긴 수량
            - `price` (int): 메뉴 단가
            - `menuUrl` (String): 메뉴 이미지 URL
          - `totalPrice` (int): 장바구니 전체 금액 (메뉴 단가 × 수량 합계)

          ### 사용 방법
          1. 로그인 후 JWT 인증이 완료된 상태에서 요청합니다.
          2. 메뉴 ID와 수량을 전달하면,
             - 이미 장바구니에 존재하는 메뉴인 경우 수량이 변경됩니다.
             - 장바구니에 없는 메뉴인 경우 새로 추가됩니다.
          3. 수량을 0으로 전달하면 해당 메뉴는 장바구니에서 삭제됩니다.
          4. 장바구니에 담긴 모든 메뉴가 삭제되면 장바구니 자체도 함께 삭제됩니다.
          5. 처리 완료 후 현재 장바구니 상태를 응답으로 반환합니다.

          ### 유의 사항
          - 인증되지 않은 사용자는 요청할 수 없습니다.
          - `menu_id`는 실제 존재하는 메뉴 ID여야 합니다.
          - 수량은 음수 값을 허용하지 않습니다.
          - 장바구니는 사용자당 하나만 생성되며, 존재하지 않을 경우 자동 생성됩니다.
          - 장바구니가 완전히 비워지면 DB에서 장바구니 엔티티가 삭제됩니다.

          ### 예외 처리
          - `MENU_NOT_FOUND` (404 NOT_FOUND): 해당 메뉴를 찾을 수 없습니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): 인증되지 않은 사용자의 접근
          """
  )
  ResponseEntity<CartResponse> addOrUpdateItems(
      @RequestBody CartRequest request
  );

  @Operation(
      summary = "장바구니 조회",
      description = """
          ### 요청 파라미터
          - 없음 (JWT 인증 정보로 사용자 식별)

          ### 응답 데이터
          - `cartId` (Long): 장바구니 ID (존재하는 경우에만 반환)
          - `items` (List<CartItemDto>): 장바구니에 담긴 메뉴 목록
            - `cartItemId` (Long): 장바구니 아이템 ID
            - `menuName` (String): 메뉴 이름
            - `quantity` (int): 담긴 수량
            - `price` (int): 메뉴 단가
            - `menuUrl` (String): 메뉴 이미지 URL
          - `totalPrice` (int): 장바구니 전체 금액

          ### 사용 방법
          1. 로그인 후 JWT 인증이 완료된 상태에서 요청합니다.
          2. 사용자의 장바구니가 존재하면 현재 장바구니 정보를 반환합니다.
          3. 장바구니가 존재하지 않는 경우,
             - `items`는 빈 배열
             - `totalPrice`는 0
             - `cartId`는 반환되지 않습니다.

          ### 유의 사항
          - 장바구니가 없는 경우 새로 생성하지 않습니다.
          - 조회 API는 장바구니 상태를 변경하지 않습니다.

          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): 인증되지 않은 사용자의 접근
          """
  )
  ResponseEntity<CartResponse> getCart();

  @Operation(
      summary = "장바구니 삭제",
      description = """
          ### 요청 파라미터
          - `cartId` (Long, required): 삭제할 장바구니 ID

          ### 응답 데이터
          - 없음 (HTTP 200 OK)

          ### 사용 방법
          1. 삭제할 장바구니 ID를 Path Variable로 전달합니다.
          2. 해당 장바구니가 DB에서 삭제됩니다.

          ### 유의 사항
          - 장바구니 ID는 실제 존재하는 값이어야 합니다.
          - 장바구니 삭제 시, 해당 장바구니에 속한 모든 아이템도 함께 삭제됩니다.
          - 인증된 사용자만 요청할 수 있습니다.

          ### 예외 처리
          - `INVALID_REQUEST` (400 BAD_REQUEST): 잘못된 요청 형식
          """
  )
  void deleteCart(@PathVariable Long cartId);

  @Operation(
      summary = "장바구니 특정 메뉴 삭제",
      description = """
          ### 요청 파라미터
          - `cartItemId` (Long, required): 삭제할 장바구니 아이템 ID

          ### 응답 데이터
          - 없음 (HTTP 200 OK)

          ### 사용 방법
          1. 삭제하려는 장바구니 아이템 ID를 Path Variable로 전달합니다.
          2. 해당 장바구니 아이템이 DB에서 삭제됩니다.

          ### 유의 사항
          - 장바구니 아이템 ID는 실제 존재하는 값이어야 합니다.
          - 인증된 사용자만 요청할 수 있습니다.

          ### 예외 처리
          - `INVALID_REQUEST` (400 BAD_REQUEST): 잘못된 요청 형식
          """
  )
  void deleteCartItem(@PathVariable Long cartItemId);
}
