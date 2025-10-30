package com.campustable.be.domain.menu.exception;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;

public class MenuNotFoundException extends CustomException {
    public MenuNotFoundException() {super(ErrorCode.MENU_NOT_FOUND);}
}
