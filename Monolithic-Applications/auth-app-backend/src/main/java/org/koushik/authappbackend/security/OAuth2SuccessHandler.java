package org.koushik.authappbackend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.koushik.authappbackend.model.Provider;
import org.koushik.authappbackend.model.RefreshToken;
import org.koushik.authappbackend.model.User;
import org.koushik.authappbackend.repository.RefreshTokenRepository;
import org.koushik.authappbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final RefreshTokenRepository refreshTokenRepository;

    private User findOrCreateUser(String email, String name, String picture, Provider provider) {
        return userRepository.findUserByEmail(email)
                .orElseGet(() ->
                        userRepository.save(
                                User.builder()
                                        .email(email)
                                        .fullName(name)
                                        .imageUrl(picture)
                                        .provider(provider)
                                        .enabled(true)
                                        .build()
                        )
                );
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Successful authentication");
        logger.info("Authenticated user: {}", authentication.getName());

        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
           throw new IllegalArgumentException("OAuth2 authentication required");
        }

        OAuth2User oAuth2User = token.getPrincipal();
        String registrationId = token.getAuthorizedClientRegistrationId();


        logger.info("Registration Id: {}", registrationId);
        logger.debug("OAuth2 attributes: {}", oAuth2User.getAttributes());


        User user;
        switch (registrationId) {
            case "google" -> {
                String email = oAuth2User.getAttribute("email");
                String name = oAuth2User.getAttribute("name");
                String picture = oAuth2User.getAttribute("picture");

                user = findOrCreateUser(
                        email, name, picture, Provider.GOOGLE
                );
            }
            case "github" -> {
                String name = oAuth2User.getAttribute("name");
                String picture = oAuth2User.getAttribute("avatar_url");
                String email = (String) oAuth2User.getAttributes().get("email");

                if (email == null) {
                    throw new IllegalStateException("Github email is not available");
                }

                user = findOrCreateUser(
                        email,
                        name,
                        picture,
                        Provider.GITHUB
                );
            }
            default -> throw new RuntimeException("Invalid registration id");

        }

        String jti = UUID.randomUUID().toString();
        Instant now = Instant.now();
        RefreshToken refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .revoked(false)
                .createdAt(now)
                .expiresAt(now.plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();

        refreshTokenRepository.save(refreshTokenOb);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());

        response.getWriter().write("Login successful");
    }
}
