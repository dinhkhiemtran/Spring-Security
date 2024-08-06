package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.exception.JwKeyNotRegisteredException;
import com.khiemtran.service.SecretKeyService;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class SecretKeyServiceImpl implements SecretKeyService {
  @Override
  public SecretKey getKey(YamlConfig yamlConfig) {
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
}
