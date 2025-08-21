package com.roudane.inventory.infra.transverse.exception;


import com.roudane.transverse.enums.ErrorCode;
import com.roudane.transverse.exception.BusinessException;
import com.roudane.transverse.module.ApiError;
import com.roudane.transverse.module.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleApiException(BusinessException ex, HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .status(ex.getStatus())
                .timestamp(LocalDateTime.now())
                .path(request.getServletPath())
                .message(ex.getMessage())
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getStatus().getCode()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Field> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Field.builder().field(error.getField()).message(error.getDefaultMessage()).build())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(ErrorCode.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .path(request.getServletPath())
                .details(fieldErrors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


}
