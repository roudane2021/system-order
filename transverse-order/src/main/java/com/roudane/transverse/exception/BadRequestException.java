package com.roudane.transverse.exception;

import com.roudane.transverse.enums.ErrorCode;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
