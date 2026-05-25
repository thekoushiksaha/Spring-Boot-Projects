package org.koushik.jwtsecurityrefreshtoken01.config;

import lombok.RequiredArgsConstructor;
import org.koushik.jwtsecurityrefreshtoken01.entity.Role;
import org.koushik.jwtsecurityrefreshtoken01.entity.User;
import org.koushik.jwtsecurityrefreshtoken01.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                        .name("Koushik Saha")
                        .email("thekoushiksaha@zohomail.in")
                        .username("admin")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ROLE_ADMIN)
                        .build();

                userRepository.save(user);
            }
        };
    }
}
