package com.campustable.be.domain.menu.exception;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;

public class MenuAlreadyExistsException extends CustomException {
    public MenuAlreadyExistsException() {super(ErrorCode.MENU_ALREADY_EXISTS);}
}
