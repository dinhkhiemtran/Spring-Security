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

@Service
@RequiredArgsConstructor
public class SecretKeyServiceImpl implements SecretKeyService {
  private final YamlConfig yamlConfig;

  @Override
  public SecretKey getKey(TokenType type) {
    String secret = ACCESS_TOKEN.equals(type) ? yamlConfig.getJwt().getJwtSecret() : yamlConfig.getJwt().getRefreshJwtSecret();
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public static String randomJwtSecret() {
    byte[] key = new byte[128];
    new SecureRandom().nextBytes(key);
    return Base64.getEncoder().encodeToString(key);
  }
}