package org.koushik.jwtsecurityrefreshtoken01.repository;

import org.koushik.jwtsecurityrefreshtoken01.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
