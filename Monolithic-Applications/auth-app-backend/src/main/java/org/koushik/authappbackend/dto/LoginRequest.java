package org.koushik.authappbackend.dto;

public record LoginRequest(
        String username,
        String password
) {
}
