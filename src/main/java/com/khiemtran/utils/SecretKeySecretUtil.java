package com.khiemtran.utils;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.exception.JwKeyNotRegisteredException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(prefix = "spring.application", name = "jwt-secret")
public class SecretKeySecretUtil {
  private final YamlConfig yamlConfig;

  public SecretKey getKey() {
    String jwtSecret = yamlConfig.getJwtSecret();
    if (!jwtSecret.isEmpty()) {
      byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
      return Keys.hmacShaKeyFor(keyBytes);
    }
    throw new JwKeyNotRegisteredException("JWK has not been registered");
  }
}
