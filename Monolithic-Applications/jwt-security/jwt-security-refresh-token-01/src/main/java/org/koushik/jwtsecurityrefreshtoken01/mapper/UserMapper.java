package org.koushik.jwtsecurityrefreshtoken01.mapper;

import org.koushik.jwtsecurityrefreshtoken01.dto.request.RegisterRequest;
import org.koushik.jwtsecurityrefreshtoken01.entity.Role;
import org.koushik.jwtsecurityrefreshtoken01.entity.User;

import java.time.Instant;

public class UserMapper {
    public static User toEntity(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.ROLE_USER)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}
