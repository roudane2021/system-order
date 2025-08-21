package com.roudane.transverse.exception;

import com.roudane.transverse.enums.ErrorCode;

public class InternalErrorException extends BusinessException {
    public InternalErrorException(String message) {
        super(ErrorCode.INTERNAL_ERROR, message);
    }
}
