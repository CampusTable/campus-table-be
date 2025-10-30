package com.campustable.be.domain.cafeteria.exception;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;

public class CafeteriaAlreadyExistsException extends CustomException {
  public CafeteriaAlreadyExistsException() {
    super(ErrorCode.CAFETERIA_ALREADY_EXISTS);
  }
}
