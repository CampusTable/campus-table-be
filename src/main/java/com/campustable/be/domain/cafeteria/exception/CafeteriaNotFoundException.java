package com.campustable.be.domain.cafeteria.exception;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;

public class CafeteriaNotFoundException extends CustomException {
  public CafeteriaNotFoundException() {
    super(ErrorCode.CAFETERIA_NOT_FOUND);
  }
}
