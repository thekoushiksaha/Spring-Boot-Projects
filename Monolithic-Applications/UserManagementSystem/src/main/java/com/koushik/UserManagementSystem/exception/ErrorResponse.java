package com.koushik.UserManagementSystem.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "message",
        "status",
        "path",
        "errors"
})
public class ErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp =  LocalDateTime.now();
    private String message;
    private Integer status;
    private String path;
    private Map<String, String> errors;

    public ErrorResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(String message, Integer status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
    }

    public ErrorResponse(String message, Integer status, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }
}
