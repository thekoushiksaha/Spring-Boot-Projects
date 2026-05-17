package org.koushik.jwtsecurityproject.service;

import org.koushik.jwtsecurityproject.dto.LoginRequest;

import java.util.Map;

public interface JwtAuthService {
    Map<String, String> register(LoginRequest loginRequest);
    Map<String, String> login(LoginRequest loginRequest);
}
