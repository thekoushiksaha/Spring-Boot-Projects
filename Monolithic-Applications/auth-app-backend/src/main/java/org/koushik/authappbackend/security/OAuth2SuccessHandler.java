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

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Successful authentication");
        logger.info(authentication.toString());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = "unknown";

        if (authentication instanceof OAuth2AuthenticationToken token) {
            registrationId = token.getAuthorizedClientRegistrationId();
        }

        logger.info("Registration Id: " + registrationId);
        logger.info("User: " + oAuth2User.getAttributes().toString());

        User user = null;

        switch (registrationId) {
            case "google" -> {
                String googleId = oAuth2User.getAttributes().getOrDefault("sub", "").toString();
                String email = oAuth2User.getAttributes().getOrDefault("email", "").toString();
                String name = oAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("picture", "").toString();

                user = userRepository.findUserByEmail(email)
                        .orElseGet(() ->
                                userRepository.save(
                                        User.builder()
                                                .email(email)
                                                .fullName(name)
                                                .imageUrl(picture)
                                                .provider(Provider.GOOGLE)
                                                .enabled(true)
                                                .build()
                                )
                        );
            }
            case "github" -> {
                String name = oAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("avatar_url", "").toString();

                String email = (String) oAuth2User.getAttributes().get("email");

                user = userRepository.findUserByEmail(email)
                        .orElseGet(() ->
                                userRepository.save(
                                        User.builder()
                                                .email(email)
                                                .fullName(name)
                                                .imageUrl(picture)
                                                .provider(Provider.GITHUB)
                                                .enabled(true)
                                                .build()
                                )
                        );
            }
            default -> {
                throw new RuntimeException("Invalid registration id");
            }
        }

        if (user != null) {
            String jti = UUID.randomUUID().toString();
            RefreshToken refreshTokenOb = RefreshToken.builder()
                    .jti(jti)
                    .user(user)
                    .revoked(false)
                    .createdAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                    .build();

            refreshTokenRepository.save(refreshTokenOb);

            String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

            cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
        }
        response.getWriter().write("Login successful");
    }
}
