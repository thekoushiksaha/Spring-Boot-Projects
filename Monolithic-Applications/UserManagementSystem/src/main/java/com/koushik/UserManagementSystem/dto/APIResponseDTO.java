package com.koushik.UserManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        {"timestamp", "message", "status", "data"}
)
public class APIResponseDTO<T> {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp =  LocalDateTime.now();
    private String message;
    private int status;
    private T data;

    public  APIResponseDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public APIResponseDTO(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
