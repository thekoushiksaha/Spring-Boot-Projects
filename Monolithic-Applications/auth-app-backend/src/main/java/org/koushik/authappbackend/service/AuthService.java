package org.koushik.authappbackend.service;

import jakarta.servlet.http.HttpServletResponse;
import org.koushik.authappbackend.dto.LoginRequest;
import org.koushik.authappbackend.dto.TokenResponse;
import org.koushik.authappbackend.dto.UserDto;

public interface AuthService {
    UserDto register(UserDto userDto);
    TokenResponse login(LoginRequest loginRequest, HttpServletResponse response);
}
