package com.campustable.be.domain.auth.exception;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AuthenticationFailedException extends CustomException {

  public AuthenticationFailedException() {
    super(ErrorCode.AUTH_FAILED);
  }

}