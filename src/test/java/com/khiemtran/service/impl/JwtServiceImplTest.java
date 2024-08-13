package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;
import com.khiemtran.model.User;
import com.khiemtran.service.SecretKeyService;
import com.khiemtran.utils.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = JwtServiceImpl.class)
@ActiveProfiles("test")
@SpringBootTest
class JwtServiceImplTest {
  @InjectMocks
  private JwtServiceImpl jwtService;
  @MockBean
  private SecretKeyService secretKeyService;
  @MockBean
  private YamlConfig yamlConfig;
  private UserPrincipal userPrincipal;
  private SecretKey secretKeyAccessToken;
  private SecretKey secretKeyRefreshToken;
  private String accessToken;
  private String refreshToken;

  @BeforeEach
  public void init() {
    ReflectionTestUtils.setField(jwtService, "secretKeyService", secretKeyService);
    ReflectionTestUtils.setField(jwtService, "yamlConfig", yamlConfig);
    secretKeyAccessToken = mock(SecretKey.class);
    secretKeyRefreshToken = mock(SecretKey.class);
    User user = new User("username", "password", "email@mail", "zipCode", "city");
    userPrincipal = new UserPrincipal(user);
    String secret = "501fdb5b323041aaab32af43c045403323496d489bc3d98b2e00a8000bdd89b5";
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    secretKeyAccessToken = Keys.hmacShaKeyFor(keyBytes);
    secretKeyRefreshToken = Keys.hmacShaKeyFor(keyBytes);
    when(secretKeyService.getKey(yamlConfig, TokenType.ACCESS_TOKEN)).thenReturn(secretKeyAccessToken);
    when(secretKeyService.getKey(yamlConfig, TokenType.REFRESH_TOKEN)).thenReturn(secretKeyRefreshToken);
    jwtService.init();
    Date now = new Date();
    long expireTime = now.getTime() + 1000 * 60 * 5;
    long expireDay = now.getTime() + 1000 * 60 * 60 * 24 * 5;
    accessToken = jwtService.generateToken(userPrincipal, TokenType.ACCESS_TOKEN, expireTime);
    refreshToken = jwtService.generateRefreshToken(userPrincipal, TokenType.REFRESH_TOKEN, expireDay);
  }

  @Test
  public void generateAccessToken() {
    String email = jwtService.extractToken(accessToken, TokenType.ACCESS_TOKEN);
    String city = extract(accessToken, secretKeyAccessToken, claims -> claims.get("city", String.class));
    String zipcode = extract(accessToken, secretKeyAccessToken, claims -> claims.get("zipCode", String.class));
    Assertions.assertEquals("email@mail", email);
    Assertions.assertEquals("city", city);
    Assertions.assertEquals("zipCode", zipcode);
  }

  @Test
  public void generateRefreshToken() {
    String email = jwtService.extractToken(refreshToken, TokenType.REFRESH_TOKEN);
    String city = extract(refreshToken, secretKeyRefreshToken, claims -> claims.get("city", String.class));
    String zipcode = extract(refreshToken, secretKeyRefreshToken, claims -> claims.get("zipCode", String.class));
    Assertions.assertEquals("email@mail", email);
    Assertions.assertEquals("city", city);
    Assertions.assertEquals("zipCode", zipcode);
  }

  @Test
  public void invalidationAccessToken() {
    User user1 = new User("username1", "password1", "email1@mail", "zipCode1", "city1");
    UserPrincipal principal = new UserPrincipal(user1);
    boolean actual = jwtService.isValidationToken(accessToken, TokenType.ACCESS_TOKEN, principal);
    Assertions.assertFalse(actual);
  }

  @Test
  public void invalidationRefreshToken() {
    User user1 = new User("username1", "password1", "email1@mail", "zipCode1", "city1");
    UserPrincipal principal = new UserPrincipal(user1);
    boolean actual = jwtService.isValidationToken(refreshToken, TokenType.REFRESH_TOKEN, principal);
    Assertions.assertFalse(actual);
  }

  @Test
  public void accessTokenExpired() {
    boolean actual = jwtService.isValidationToken(accessToken, TokenType.ACCESS_TOKEN, userPrincipal);
    Assertions.assertTrue(actual);
  }

  @Test
  public void refreshTokenExpired() {
    boolean actual = jwtService.isValidationToken(refreshToken, TokenType.REFRESH_TOKEN, userPrincipal);
    Assertions.assertTrue(actual);
  }

  private <T> T extract(String token, SecretKey secretKey, Function<Claims, T> function) {
    Claims extract = extract(token, secretKey);
    return function.apply(extract);
  }

  private Claims extract(String token, SecretKey secretKey) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}