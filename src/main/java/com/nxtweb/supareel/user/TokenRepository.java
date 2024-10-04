package com.nxtweb.supareel.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
    List<Token> findByUserIdAndExpiresAtAfterAndValidatedAtIsNull(Integer user_id, LocalDateTime expiresAt);
}
