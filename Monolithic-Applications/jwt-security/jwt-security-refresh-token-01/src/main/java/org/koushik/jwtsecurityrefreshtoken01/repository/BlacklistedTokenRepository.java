package org.koushik.jwtsecurityrefreshtoken01.repository;

import org.koushik.jwtsecurityrefreshtoken01.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken,Integer> {
    Optional<BlacklistedToken> findByToken(String refreshToken);
}
