package org.koushik.jwtsecurityproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koushik.jwtsecurityproject.dto.LoginRequest;
import org.koushik.jwtsecurityproject.service.JwtAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class JwtAuthController {
    private final JwtAuthService jwtAuthService;

    @GetMapping("/info")
    public Map<String, String> info(){
        Map<String, String> map = new HashMap<>();
        map.put("api_controller","JwtAuthController");
        return map;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody LoginRequest request){
        return new ResponseEntity<>(jwtAuthService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request){
       return new ResponseEntity<>(jwtAuthService.login(request), HttpStatus.OK);
    }
}
