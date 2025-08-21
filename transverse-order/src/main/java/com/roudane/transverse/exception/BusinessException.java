package com.roudane.transverse.exception;

import com.roudane.transverse.enums.ErrorCode;


public class BusinessException  extends RuntimeException {
    private final ErrorCode status;

    protected BusinessException(ErrorCode status, String message) {
        super(message);
        this.status = status;
    }

    public ErrorCode getStatus() {
        return status;
    }
}
