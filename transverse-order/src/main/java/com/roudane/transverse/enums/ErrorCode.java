package com.roudane.transverse.enums;

public enum ErrorCode {
    NOT_FOUND(404),
    BAD_REQUEST(400),
    INTERNAL_ERROR(500),
    UNAUTHORIZED(401);

    private final  int code;
    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
