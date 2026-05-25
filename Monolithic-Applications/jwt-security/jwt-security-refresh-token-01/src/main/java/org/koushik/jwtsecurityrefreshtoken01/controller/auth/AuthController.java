package org.koushik.jwtsecurityrefreshtoken01.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.LoginRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.RefreshTokenRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.RegisterRequest;
import org.koushik.jwtsecurityrefreshtoken01.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService  authService;

    @GetMapping("/info")
    public String info(){
        return "Auth controller";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestHeader("Authorization") String authHeader){
        return new ResponseEntity<>(authService.logout(authHeader), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return new ResponseEntity<>(authService.refreshToken(refreshTokenRequest), HttpStatus.CREATED);
    }
}
