package org.koushik.authappbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.koushik.authappbackend.dto.LoginRequest;
import org.koushik.authappbackend.dto.TokenResponse;
import org.koushik.authappbackend.dto.UserDto;
import org.koushik.authappbackend.exception.UserNotFoundException;
import org.koushik.authappbackend.model.RefreshToken;
import org.koushik.authappbackend.model.User;
import org.koushik.authappbackend.repository.RefreshTokenRepository;
import org.koushik.authappbackend.repository.UserRepository;
import org.koushik.authappbackend.security.JwtService;
import org.koushik.authappbackend.service.AuthService;
import org.koushik.authappbackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDto register(UserDto userDto) {
        return userService.createUser(userDto);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found")
                );

        String jti = UUID.randomUUID().toString();
        var refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenOb);


        // Generate access token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

        return TokenResponse.of(
                accessToken,
                refreshToken,
                jwtService.getAccessTtlSeconds(),
                modelMapper.map(user, UserDto.class)
        );
    }
}
