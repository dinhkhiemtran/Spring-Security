package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;
import com.khiemtran.service.JwtService;
import com.khiemtran.service.SecretKeyService;
import com.khiemtran.utils.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
  private final SecretKeyService secretKeyService;
  private final YamlConfig yamlConfig;

  @Override
  public String generateToken(UserPrincipal userPrincipal, TokenType tokenType, long expireTime) {
    return generateToken(new HashMap<>(), tokenType, userPrincipal, expireTime);
  }

  @Override
  public String generateRefreshToken(UserPrincipal userPrincipal, TokenType tokenType, long expireDay) {
    return generateRefreshToken(new HashMap<>(), tokenType, userPrincipal, expireDay);
  }

  @Override
  public String extractToken(String token, TokenType type) {
    return extractClaim(token, type, Claims::getSubject);
  }

  private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claim) {
    return claim.apply(extractAllClaims(token, type));
  }

  @Override
  public boolean isValidationToken(String token, TokenType type, UserPrincipal userPrincipal) {
    String email = extractToken(token, type);
    return email.equals(userPrincipal.getEmail()) && !isTokenExpired(token, type);
  }

  private Claims extractAllClaims(String token, TokenType type) {
    return Jwts.parserBuilder()
        .setSigningKey(getKey(type))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private SecretKey getKey(TokenType type) {
    return secretKeyService.getKey(yamlConfig, type);
  }

  private boolean isTokenExpired(String token, TokenType type) {
    return extractExpiration(token, type).before(new Date());
  }

  private Date extractExpiration(String token, TokenType type) {
    return extractClaim(token, type, Claims::getExpiration);
  }

  private String generateToken(Map<String, Object> claims, TokenType tokenType, UserPrincipal userPrincipal, long expireTime) {
    claims.put("city", userPrincipal.getCity());
    claims.put("zipCode", userPrincipal.getZipCode());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userPrincipal.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(expireTime))
        .signWith(getKey(tokenType), SignatureAlgorithm.HS256)
        .compact();
  }

  private String generateRefreshToken(Map<String, Object> claims, TokenType tokenType, UserPrincipal userPrincipal, long expireTime) {
    claims.put("city", userPrincipal.getCity());
    claims.put("zipCode", userPrincipal.getZipCode());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userPrincipal.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(expireTime))
        .signWith(getKey(tokenType), SignatureAlgorithm.HS256)
        .compact();
  }
}
