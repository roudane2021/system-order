package com.roudane.transverse.module;

import com.roudane.transverse.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
    private ErrorCode status;
    private String error;
    private String message;
    private String path;
    private List<Field> details;
    private LocalDateTime timestamp;
}
