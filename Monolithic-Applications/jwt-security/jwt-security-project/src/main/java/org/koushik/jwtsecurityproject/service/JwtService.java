package org.koushik.jwtsecurityproject.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    boolean isValidToken(String token);
    String extractUsername(String token);
}
