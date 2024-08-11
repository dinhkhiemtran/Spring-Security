package com.khiemtran.repository;

import com.khiemtran.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByEmail(String email);
  boolean existsByEmail(String email);
}
