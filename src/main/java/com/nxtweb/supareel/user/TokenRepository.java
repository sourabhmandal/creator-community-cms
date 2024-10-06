package com.nxtweb.supareel.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByToken(String token);
    List<Token> findByUserIdAndExpiresAtAfterAndValidatedAtIsNull(UUID user_id, LocalDateTime expiresAt);
}
