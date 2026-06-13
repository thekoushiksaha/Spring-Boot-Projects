package org.koushik.jwtsecurityproject.service;

import lombok.RequiredArgsConstructor;
import org.koushik.jwtsecurityproject.dto.LoginRequest;
import org.koushik.jwtsecurityproject.model.Role;
import org.koushik.jwtsecurityproject.model.User;
import org.koushik.jwtsecurityproject.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Map<String, String> register(LoginRequest loginRequest) {
        Map<String, String> result = new HashMap<>();
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        result.put("message", "success");
        result.put("username", user.getUsername());
        result.put("role", user.getRole().toString());
        return result;
    }

    @Override
    public Map<String, String> login(LoginRequest loginRequest) {
        Map<String, String> result = new HashMap<>();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtService.generateToken(userDetails);
        result.put("username", loginRequest.getUsername());
        result.put("token", token);
        return result;
    }
}
