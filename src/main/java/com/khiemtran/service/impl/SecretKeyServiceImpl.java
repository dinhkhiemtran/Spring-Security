package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;
import com.khiemtran.service.SecretKeyService;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import static com.khiemtran.constants.TokenType.ACCESS_TOKEN;
import static com.khiemtran.constants.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class SecretKeyServiceImpl implements SecretKeyService {
  private final YamlConfig yamlConfig;

  @Override
  public SecretKey getKey(TokenType type) {
    YamlConfig.Jwt jwt = yamlConfig.getJwt();
    String jwtSecret = jwt.getJwtSecret();
    String refreshJwtSecret = jwt.getRefreshJwtSecret();
    byte[] keyBytes = new byte[0];
    if (ACCESS_TOKEN.equals(type)) {
      keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    }
    if (REFRESH_TOKEN.equals(type)) {
      keyBytes = refreshJwtSecret.getBytes(StandardCharsets.UTF_8);
    }
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public static String randomJwtSecret() {
    SecureRandom random = new SecureRandom();
    byte[] key = new byte[128];
    random.nextBytes(key);
    return Base64.getEncoder().encodeToString(key);
  }
}
