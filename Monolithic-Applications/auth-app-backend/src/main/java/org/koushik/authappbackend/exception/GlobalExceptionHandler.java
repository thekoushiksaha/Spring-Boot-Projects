package org.koushik.authappbackend.exception;

import org.koushik.authappbackend.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        Instant now = Instant.now();
        ZonedDateTime indiaTime = now.atZone(ZoneId.of("Asia/Kolkata"));
        ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(indiaTime)
                .message(userNotFoundException.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        Instant now = Instant.now();
        ZonedDateTime indiaTime = now.atZone(ZoneId.of("Asia/Kolkata"));
        ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(indiaTime)
                .message(illegalArgumentException.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
}
