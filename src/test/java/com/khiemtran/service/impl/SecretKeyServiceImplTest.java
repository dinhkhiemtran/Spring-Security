package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@ContextConfiguration(classes = SecretKeyServiceImpl.class)
@ActiveProfiles("test")
@SpringBootTest
class SecretKeyServiceImplTest {
  @InjectMocks
  private SecretKeyServiceImpl secretKeyService;
  @MockBean
  private YamlConfig yamlConfig;
  private static final String SECRET_ACCESS_TOKEN = "501fdb5b323041aaab32af43c045403323496d489bc3d98b2e00a8000bdd89b5";
  private static final String SECRET_REFRESH_TOKEN = "aea3b5683ec3ab09ab125767aefa975ab588d5da52f6d95e90ea8fb1cd08a8c0";

  @BeforeEach
  public void init() {
    ReflectionTestUtils.setField(secretKeyService, "yamlConfig", yamlConfig);
    YamlConfig.Jwt jwt = new YamlConfig.Jwt();
    jwt.setJwtSecret(SECRET_ACCESS_TOKEN);
    jwt.setRefreshJwtSecret(SECRET_REFRESH_TOKEN);
    jwt.setExpireDay(300);
    jwt.setExpireDay(5);
    Mockito.when(yamlConfig.getJwt()).thenReturn(jwt);
  }

  @Test
  public void getKeyAccessToken() {
    SecretKey actual = secretKeyService.getKey(TokenType.ACCESS_TOKEN);
    byte[] keyBytes = SECRET_ACCESS_TOKEN.getBytes(StandardCharsets.UTF_8);
    SecretKey expect = Keys.hmacShaKeyFor(keyBytes);
    Assertions.assertEquals(expect, actual);
  }

  @Test
  public void getKeyRefreshToken() {
    SecretKey actual = secretKeyService.getKey(TokenType.REFRESH_TOKEN);
    byte[] keyBytes = SECRET_REFRESH_TOKEN.getBytes(StandardCharsets.UTF_8);
    SecretKey expect = Keys.hmacShaKeyFor(keyBytes);
    Assertions.assertEquals(expect, actual);
  }
}