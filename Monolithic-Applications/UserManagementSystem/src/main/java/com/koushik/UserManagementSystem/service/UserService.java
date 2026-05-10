package com.koushik.UserManagementSystem.service;

import com.koushik.UserManagementSystem.dto.APIResponseDTO;
import com.koushik.UserManagementSystem.dto.UserDTO;
import com.koushik.UserManagementSystem.dto.UserRequestDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    APIResponseDTO<UserDTO> createUser(UserRequestDTO userRequestDTO);

    APIResponseDTO<UserDTO> getUserById(Integer id);

    APIResponseDTO<String> deleteUserById(Integer id);

    APIResponseDTO<UserDTO> updateUser(Integer id, UserRequestDTO userRequestDTO);

    APIResponseDTO<List<UserDTO>> getUsers();
}
