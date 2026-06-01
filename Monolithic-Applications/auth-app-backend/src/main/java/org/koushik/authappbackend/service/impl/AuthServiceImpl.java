package org.koushik.authappbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.koushik.authappbackend.dto.UserDto;
import org.koushik.authappbackend.service.AuthService;
import org.koushik.authappbackend.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    @Override
    public UserDto register(UserDto userDto) {
        return userService.createUser(userDto);
    }
}
