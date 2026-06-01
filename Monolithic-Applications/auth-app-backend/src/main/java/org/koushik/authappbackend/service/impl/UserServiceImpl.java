package org.koushik.authappbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.koushik.authappbackend.dto.UserDto;
import org.koushik.authappbackend.model.Provider;
import org.koushik.authappbackend.model.User;
import org.koushik.authappbackend.exception.UserNotFoundException;
import org.koushik.authappbackend.helper.UserHelper;
import org.koushik.authappbackend.repository.UserRepository;
import org.koushik.authappbackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        if (userRepository.existsUserByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User already exist with same email");
        }
        if (userDto.getUsername() == null || userDto.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        //Roles will cover later
        //TODO:
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String id) {
        User existingUser = userRepository
                .findById(UserHelper.parseUUID(id))
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with id " + id)
                );
        // We are not going to change email id

        if (userDto.getFullName() != null) existingUser.setFullName(userDto.getFullName());
        if (userDto.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getImageUrl() != null) existingUser.setImageUrl(userDto.getImageUrl());
        if (userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        if (userDto.getEnabled() != null) {
            existingUser.setEnabled(userDto.getEnabled());
        }
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUserById(String id) {
        User existingUser = userRepository.findById(UserHelper.parseUUID(id))
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with id " + id)
                );
        userRepository.delete(existingUser);
    }

    @Override
    public UserDto getUserById(String id) {
        User user = userRepository
                .findById(UserHelper.parseUUID(id))
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with id " + id)
                );
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository
                .findUserByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with email: " + email)
                );
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}

