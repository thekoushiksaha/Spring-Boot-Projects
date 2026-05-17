package org.koushik.jwtsecurityproject.controller;

import lombok.RequiredArgsConstructor;
import org.koushik.jwtsecurityproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<Map<String,String>> info() {
        return new ResponseEntity<>(userService.getApiInfo(), HttpStatus.OK);
    }
}
