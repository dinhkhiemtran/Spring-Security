package com.khiemtran.utils;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.exception.JwKeyNotRegisteredException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(prefix = "spring.application.jwt", name = "jwt-secret")
public class SecretKeySecretUtil {
  private final YamlConfig yamlConfig;

  public SecretKey getKey() {
    YamlConfig.Jwt jwt = yamlConfig.getJwt();
    String jwtSecret = jwt.getJwtSecret();
    if (!jwtSecret.isEmpty()) {
      byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
      return Keys.hmacShaKeyFor(keyBytes);
    }
    throw new JwKeyNotRegisteredException("JWK has not been registered");
  }

  public static String randomJwtSecret() {
    SecureRandom random = new SecureRandom();
    byte[] key = new byte[128];
    random.nextBytes(key);
    return Base64.getEncoder().encodeToString(key);
  }

  public static void main(String[] args) {
    System.out.println(randomJwtSecret());
  }
}
