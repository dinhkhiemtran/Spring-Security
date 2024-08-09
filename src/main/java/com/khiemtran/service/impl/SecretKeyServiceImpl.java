package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;
import com.khiemtran.service.SecretKeyService;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import static com.khiemtran.constants.TokenType.ACCESS_TOKEN;
import static com.khiemtran.constants.TokenType.REFRESH_TOKEN;

@Service
public class SecretKeyServiceImpl implements SecretKeyService {
  @Override
  public SecretKey getKey(YamlConfig yamlConfig, TokenType type) {
    YamlConfig.Jwt jwt = yamlConfig.getJwt();
    String jwtSecret = jwt.getJwtSecret();
    String refreshJwtSecret = jwt.getRefreshJwtSecret();
    if (StringUtils.isBlank(jwtSecret) || StringUtils.isBlank(refreshJwtSecret)) {
      throw new RuntimeException();
    }
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
