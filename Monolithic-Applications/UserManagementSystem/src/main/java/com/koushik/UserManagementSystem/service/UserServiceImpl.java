package com.koushik.UserManagementSystem.service;

import com.koushik.UserManagementSystem.dto.APIResponseDTO;
import com.koushik.UserManagementSystem.dto.UserDTO;
import com.koushik.UserManagementSystem.dto.UserRequestDTO;
import com.koushik.UserManagementSystem.entity.User;
import com.koushik.UserManagementSystem.exception.UserNotFoundException;
import com.koushik.UserManagementSystem.exception.UsernameAlreadyExistException;
import com.koushik.UserManagementSystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public APIResponseDTO<UserDTO> createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByUserName(userRequestDTO.getUserName()).isPresent()) {
            throw new UsernameAlreadyExistException("Username already exists");
        }
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User user = new User(userRequestDTO.getFullName(), userRequestDTO.getEmail(), userRequestDTO.getUserName(), userRequestDTO.getPassword());
        User saveUser = userRepository.save(user);
        UserDTO userDTO = new UserDTO(saveUser.getId(), saveUser.getFullName(), saveUser.getEmail(), saveUser.getUserName());
        return new APIResponseDTO<>("User created successfully", HttpStatus.CREATED.value(), userDTO);
    }

    @Override
    public APIResponseDTO<UserDTO> getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        UserDTO userDTO = new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getUserName());
        return new APIResponseDTO<>("User fetched successfully", HttpStatus.OK.value(), userDTO);
    }

    @Override
    public APIResponseDTO<String> deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userRepository.delete(user);
        return new APIResponseDTO<>("User deleted successfully", HttpStatus.OK.value());
    }

    @Override
    public APIResponseDTO<UserDTO> updateUser(Integer id, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        Optional<User> userWithSameUserName = userRepository.findByUserName(userRequestDTO.getUserName());

        userWithSameUserName.ifPresent(user->{
            if(!user.getId().equals(existingUser.getId())){
                throw new UsernameAlreadyExistException("Username already exists");
            }
        });

        existingUser.setFullName(userRequestDTO.getFullName());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setUserName(userRequestDTO.getUserName());
        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        existingUser.setPassword(encodedPassword);

        User savedUser = userRepository.save(existingUser);

        UserDTO userDTO = new UserDTO(savedUser.getId(), savedUser.getFullName(), savedUser.getEmail(), savedUser.getUserName());
        return new APIResponseDTO<>("User updated successfully", HttpStatus.OK.value(), userDTO);
    }

    @Override
    public APIResponseDTO<List<UserDTO>> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : users){
            userDTOS.add(new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getUserName()));
        }
        return new APIResponseDTO<>("Users fetched successfully", HttpStatus.OK.value(), userDTOS);
    }
}
