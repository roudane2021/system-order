package com.roudane.transverse.exception;

import com.roudane.transverse.enums.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
