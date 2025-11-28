package com.campustable.be.domain.user.controller;

import com.campustable.be.domain.user.dto.UserRequest;
import com.campustable.be.domain.user.dto.UserResponse;
import com.campustable.be.domain.auth.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerDocs {

  @Operation(
      summary = "전체 사용자 조회 (관리자)",
      description = """
        ### 요청 파라미터
        - 없음
        
        ### 응답 데이터
        - 사용자 목록(List<UserResponse>)
          - `userName` (String): 사용자 이름
          - `userId` (Long): 사용자 고유 ID
          - `role` (String): 사용자 역할 (예: ADMIN, USER)

        ### 사용 방법
        - 관리자가 시스템 내 모든 사용자를 조회할 때 사용합니다.
        - 인증된 관리자 계정의 Access Token이 필요합니다.
        - 응답은 사용자 정보를 배열 형태로 전달합니다.

        ### 유의 사항
        - 본 API는 관리자만 호출할 수 있습니다.
        - 인증 토큰이 없거나 권한이 부족한 경우 요청이 거부될 수 있습니다.

        ### 예외 처리
        - **ACCESS_DENIED (403)**: 접근 권한이 없는 경우
        """
  )
  ResponseEntity<List<UserResponse>> getAllUsers();


  @Operation(
      summary = "내 정보 조회",
      description = """
        ### 요청 파라미터
        - 없음  
          (인증된 사용자의 Access Token에서 userId를 자동으로 조회)

        ### 응답 데이터 (UserResponse)
        - `userName` (String): 사용자 이름
        - `userId` (Long): 사용자 고유 ID
        - `role` (String): 사용자 역할

        ### 사용 방법
        - 로그인된 사용자가 자신의 프로필 정보를 조회할 때 사용합니다.
        - 서버는 Access Token 내부의 userId 값을 기반으로 사용자 정보를 조회합니다.

        ### 유의 사항
        - Access Token이 반드시 필요합니다.
        - 응답 데이터는 현재 DB에 저장된 최신 사용자 정보를 반영합니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          토큰은 유효하지만 해당 userId가 DB에 존재하지 않는 경우  
          ("유저를 찾을 수 없습니다.")
        """
  )
  ResponseEntity<UserResponse> getMyProfile();


  @Operation(
      summary = "내 계정 삭제",
      description = """
        ### 요청 파라미터
        - 없음  
          (인증된 Access Token의 userId 기준으로 본인 계정 삭제)

        ### 응답 데이터
        - 없음 (204 No Content)

        ### 사용 방법
        - 사용자가 자신의 계정을 삭제할 때 사용합니다.
        - 삭제 후 서버는 해당 유저 정보와 연관된 Refresh Token을 모두 삭제합니다.

        ### 유의 사항
        - 계정 삭제 시 복구가 불가능합니다.
        - 반드시 Access Token이 포함된 요청이어야 합니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          유저가 DB에 존재하지 않는 경우  
          ("유저를 찾을 수 없습니다.")
        """
  )
  ResponseEntity<Void> deleteUser();


  @Operation(
      summary = "특정 사용자 삭제 (관리자)",
      description = """
        ### 요청 파라미터
        - `userId` (Long, Path Variable): 삭제할 사용자 ID

        ### 응답 데이터
        - 없음 (204 No Content)

        ### 사용 방법
        - 관리자가 특정 사용자를 강제로 삭제할 때 사용합니다.
        - DB의 사용자 정보 및 해당 사용자의 Refresh Token이 모두 삭제됩니다.

        ### 유의 사항
        - 관리자 권한이 필요합니다.
        - 해당 userId가 존재하는지 확인 후 요청해야 합니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          삭제하려는 userId가 DB에 존재하지 않을 경우  
          ("유저를 찾을 수 없습니다.")
        """
  )
  ResponseEntity<Void> deleteUser(@PathVariable Long userId);


  @Operation(
      summary = "회원가입",
      description = """
        ### 요청 파라미터 (UserRequest)
        - `loginId` (String, required): 사용자 로그인 ID
        - `password` (String, optional): 비밀번호 (현재는 암호화 저장 X)
        - `role` (String, required): 사용자 역할

        ### 응답 데이터 (AuthResponse)
        - `isNewUser` (boolean): 신규 생성 여부 (항상 true)
        - `accessToken` (String): 로그인 후 즉시 사용할 Access Token
        - `refreshToken`: 쿠키로 전달됨 (`refreshToken` HttpOnly Cookie 생성)
        - `maxAge` (Long): Refresh Token의 유효기간(초 단위)

        ### 사용 방법
        1. 회원가입 정보를 JSON Body로 담아 요청합니다.
        2. 서버에서는 중복된 loginId가 있는지 검사합니다.
        3. 신규 유저 생성 후 Access Token과 Refresh Token을 발급합니다.
        4. Refresh Token은 HttpOnly & Secure 쿠키로 반환됩니다.

        ### 유의 사항
        - `loginId` 값은 중복될 수 없습니다.
        - `refreshToken`은 응답 Body에 포함되지 않으며 쿠키로만 전달됩니다.
        - HTTPS 환경에서만 쿠키가 정상적으로 동작합니다.

        ### 예외 처리
        - **USER_ALREADY_EXISTS (409)**:  
          이미 동일한 loginId가 존재하는 경우  
          ("이미 존재하는 유저입니다.")
        """
  )
  ResponseEntity<AuthResponse> createUser(@RequestBody UserRequest userRequest);


  @Operation(
      summary = "내 정보 수정",
      description = """
        ### 요청 파라미터 (UserRequest)
        - `loginId` (String, optional): 변경할 로그인 ID
        - `password` (String, optional): 변경할 비밀번호
        - `role` (String, optional): 변경할 역할

        ### 응답 데이터 (UserResponse)
        - `userName` (String): 사용자 이름
        - `userId` (Long): 사용자 고유 ID
        - `role` (String): 사용자 역할

        ### 사용 방법
        - 로그인된 사용자가 자신의 정보를 수정할 때 사용합니다.
        - 전달된 필드만 수정되며, 전달되지 않은 필드는 유지됩니다.

        ### 유의 사항
        - Access Token이 반드시 필요합니다.
        - 수정 가능한 값은 UserRequest DTO에 정의된 항목들(loginId, password, role)입니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          수정하려는 사용자가 DB에 존재하지 않는 경우  
          ("유저를 찾을 수 없습니다.")
        """
  )
  ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest);

}
