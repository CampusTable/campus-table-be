package com.campustable.be.domain.auth.controller;

import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.domain.auth.dto.LoginRequest;
import com.campustable.be.domain.auth.dto.ReissueRequest; // ReissueRequest 사용
import com.campustable.be.domain.auth.dto.TokenReissueResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody; // @RequestBody 사용

public interface AuthControllerDocs {
  @Operation(
      summary = "통합 로그인",
      description = """
        ### 요청 파라미터
        - `sejongPortalId` (String, required): 세종대학교 포털 로그인용 학번(ID)
        - `sejongPortalPassword` (String, required): 세종대학교 포털 로그인용 비밀번호
        
        ### 응답 데이터
        - `studentNumber` (String): 로그인된 사용자의 학번
        - `studentName` (String): 신규 회원인 경우 이름 반환, 기존 회원은 `null`
        - `isNewUser` (boolean): 신규 회원 여부 (`true`면 신규 회원)
        - `accessToken` (String): 발급된 Access Token (JWT)
        - **`refreshToken` (String): 발급된 Refresh Token (응답 본문에 포함됨)**
        - `maxAge` (Long): Refresh Token의 만료 시간(초 단위)
        
        ### 사용 방법
        1. 세종대학교 포털 계정(ID/PW)을 입력하여 로그인 요청을 보냅니다.
        2. 서버는 DB에서 학번을 조회합니다.
            - 기존 사용자: 비밀번호 검증 후 JWT 토큰 발급
            - 신규 사용자: 포털 로그인으로 인증 후 신규 사용자 등록
        3. `accessToken`과 **`refreshToken` 모두 응답 본문으로 반환됩니다.**
        4. 이후 API 요청 시 `Authorization: Bearer <accessToken>` 헤더로 인증합니다.
        
        ### 유의 사항
        - `sejongPortalId`, `sejongPortalPassword`는 모두 필수입니다.
        - **Refresh Token은 응답 본문으로 전달됩니다. (쿠키 방식 제거)**
        - 기존 회원은 `studentName` 필드가 `null`로 반환됩니다.
        
        ### 예외 처리
        - `AUTH_FAILED` (401): 아이디 또는 비밀번호가 올바르지 않은 경우
        - `SSO_AUTH_FAILED` (401): 세종대학교 포털 인증에 실패한 경우
        - `SCRAPING_STRUCTURE_CHANGED` (500): 세종대학교 포털 구조 변경으로 인해 로그인 검증 불가
        - `INTERNAL_SERVER_ERROR` (500): 서버 내부 오류 발생 시
        """
  )
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws IOException;

  @Operation(
      summary = "Access Token 재발급",
      description = """
        ### 요청 파라미터
        - (Body) `refreshToken` (String, required): 기존 로그인 시 발급받은 Refresh Token
        
        ### 응답 데이터
        - `studentNumber` (String): 사용자 학번
        - `studentName` (String): 사용자 이름
        - `isNewUser` (boolean): 신규 회원 여부 (항상 `false`)
        - `accessToken` (String): 새로 발급된 Access Token (JWT)
        - **`refreshToken` (String): 새로 발급된 Refresh Token (응답 본문에 포함됨)**
        - `maxAge` (Long): 새 Refresh Token의 만료 시간(초 단위)
        
        ### 사용 방법
        1. Access Token이 만료된 경우, 기존 Refresh Token을 **요청 본문**으로 전송하여 이 API를 호출합니다.
        2. 서버가 Refresh Token을 검증하고, 새 Access Token 및 Refresh Token을 발급하여 **응답 본문**으로 반환합니다.
        3. 새로 발급된 Access Token과 Refresh Token을 사용하여 세션을 갱신합니다.
        
        ### 유의 사항
        - Refresh Token은 요청 본문(`ReissueRequest`)을 통해 전달됩니다.
        - 유효하지 않거나 만료된 Refresh Token은 사용할 수 없습니다.
        - 새로 발급된 Refresh Token은 **응답 본문으로 전달됩니다. (쿠키 방식 제거)**
        
        ### 예외 처리
        - `REFRESH_TOKEN_NOT_FOUND` (404): 리프레시 토큰이 존재하지 않거나 비어있는 경우
        - `USER_NOT_FOUND` (404): 토큰에 매핑된 사용자를 찾을 수 없는 경우
        - `INVALID_JWT_TOKEN` (401): JWT가 만료되었거나 유효하지 않은 경우
        - `INTERNAL_SERVER_ERROR` (500): 서버 내부 오류 발생 시
        """
  )
  public ResponseEntity<TokenReissueResponse> issueAccessToken(
      @RequestBody ReissueRequest reissueRequest);
}