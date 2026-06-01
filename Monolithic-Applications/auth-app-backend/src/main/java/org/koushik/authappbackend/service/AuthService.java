package org.koushik.authappbackend.service;

import org.koushik.authappbackend.dto.UserDto;

public interface AuthService {
    UserDto register(UserDto userDto);
}
