package org.koushik.authappbackend.dto;

import lombok.*;

import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {
    private ZonedDateTime timestamp;
    private String message;
    private int status;
}
