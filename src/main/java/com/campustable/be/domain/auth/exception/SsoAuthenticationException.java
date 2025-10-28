package com.campustable.be.domain.auth.exception;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;

public class SsoAuthenticationException extends CustomException {
  public SsoAuthenticationException() {
    super(ErrorCode.SSO_AUTH_FAILED);
  }
}
