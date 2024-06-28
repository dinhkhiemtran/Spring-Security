package com.khiemtran.utils;

import com.khiemtran.config.JwtConfig;
import com.khiemtran.exception.JwKeyNotRegisteredException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(prefix = "spring.application.jwt", name = "jwt-secret")
public class SecretKeySecretUtil {
  private final JwtConfig jwtConfig;

  public SecretKey getKey() {
    String jwtSecret = jwtConfig.getJwtSecret();
    if (!jwtSecret.isEmpty()) {
      byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
      return Keys.hmacShaKeyFor(keyBytes);
    }
    throw new JwKeyNotRegisteredException("JWK has not been registered");
  }
}