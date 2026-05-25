package org.koushik.jwtsecurityrefreshtoken01.service;

import org.koushik.jwtsecurityrefreshtoken01.dto.request.LoginRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.LogoutRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.RefreshTokenRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.RegisterRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.response.LoginResponse;
import org.koushik.jwtsecurityrefreshtoken01.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    String logout(String authHeader);
}
