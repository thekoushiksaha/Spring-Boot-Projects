package com.koushik.UserManagementSystem.controller;

import com.koushik.UserManagementSystem.dto.APIResponseDTO;
import com.koushik.UserManagementSystem.dto.UserDTO;
import com.koushik.UserManagementSystem.dto.UserRequestDTO;
import com.koushik.UserManagementSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public String getAPIInfo() {
        return "Project Name: User Management System -- Users API";
    }

    @GetMapping()
    public ResponseEntity<APIResponseDTO<List<UserDTO>>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(),HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<APIResponseDTO<UserDTO>> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.createUser(userRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDTO<UserDTO>> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponseDTO<String>> deleteUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponseDTO<UserDTO>> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.updateUser(id, userRequestDTO), HttpStatus.OK);
    }

}
