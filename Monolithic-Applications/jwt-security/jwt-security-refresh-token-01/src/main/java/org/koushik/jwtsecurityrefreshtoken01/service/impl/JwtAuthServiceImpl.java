package org.koushik.jwtsecurityrefreshtoken01.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.LoginRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.LogoutRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.RefreshTokenRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.request.RegisterRequest;
import org.koushik.jwtsecurityrefreshtoken01.dto.response.LoginResponse;
import org.koushik.jwtsecurityrefreshtoken01.dto.response.UserResponse;
import org.koushik.jwtsecurityrefreshtoken01.entity.BlacklistedToken;
import org.koushik.jwtsecurityrefreshtoken01.entity.RefreshToken;
import org.koushik.jwtsecurityrefreshtoken01.entity.User;
import org.koushik.jwtsecurityrefreshtoken01.exception.InvalidRefreshTokenException;
import org.koushik.jwtsecurityrefreshtoken01.exception.UserAlreadyExistsException;
import org.koushik.jwtsecurityrefreshtoken01.exception.UserNotFoundException;
import org.koushik.jwtsecurityrefreshtoken01.exception.UserNotLoggedInException;
import org.koushik.jwtsecurityrefreshtoken01.mapper.UserMapper;
import org.koushik.jwtsecurityrefreshtoken01.repository.BlacklistedTokenRepository;
import org.koushik.jwtsecurityrefreshtoken01.repository.RefreshTokenRepository;
import org.koushik.jwtsecurityrefreshtoken01.repository.UserRepository;
import org.koushik.jwtsecurityrefreshtoken01.security.jwt.JwtService;
import org.koushik.jwtsecurityrefreshtoken01.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtAuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("username already exists");
        }

        User user = UserMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUsername());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("invalid username or password");
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        return new LoginResponse(jwtService.generateAccessToken(userDetails), jwtService.generateRefreshToken(userDetails.getUsername()).getToken());
    }

    @Override
    @Transactional
    public String logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidRefreshTokenException("Invalid authorization header");
        }
        String accessToken = authHeader.substring(7);

        String username;

        try {
            username = jwtService.extractUsername(accessToken);
        } catch (Exception e) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new UserNotLoggedInException("User already logged out"));

        blacklistedTokenRepository.save(
                BlacklistedToken.builder()
                        .token(refreshToken.getToken())
                        .loggedOutAt(Instant.now())
                        .build()
        );

        refreshTokenRepository.delete(refreshToken);

        return username + " logged out successfully";
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        if (blacklistedTokenRepository.findByToken(request.getRefreshToken()).isPresent()) {
            throw new RuntimeException("User is already logged out");
        }
        RefreshToken refreshToken = jwtService.verifyRefreshToken(request.getRefreshToken());

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(refreshToken.getUser().getUsername());

        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .token(request.getRefreshToken())
                .build();
    }
}
