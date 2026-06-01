package org.koushik.authappbackend.service;

import org.koushik.authappbackend.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, String id);
    void deleteUserById(String id);
    UserDto getUserById(String id);
    UserDto getUserByEmail(String email);
    Iterable<UserDto> getAllUsers();
}
