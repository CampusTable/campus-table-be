package com.campustable.be.global.common;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
  // 인스턴스화 방지
  private SecurityUtil() {}

  public static Long getCurrentUserId() {
    // 1. SecurityContext에서 인증 정보를 꺼냄
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 2. 인증 정보가 없거나 익명 사용자인 경우 처리
    if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
      log.error("SecurityUtil class에서 시큐리티 컨텍스트 홀더에 유저가 존재하지않아 에러발생.");
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    // 3. 저장된 유저 ID 반환 (String으로 저장되므로 Long으로 변환)

    try {
      return Long.parseLong(authentication.getName());
    } catch (NumberFormatException e) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
  }

}
