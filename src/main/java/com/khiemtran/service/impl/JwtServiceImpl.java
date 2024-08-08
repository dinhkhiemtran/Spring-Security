package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.service.JwtService;
import com.khiemtran.service.SecretKeyService;
import com.khiemtran.utils.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
  private SecretKey secretKey;

  @PostConstruct
  public void setup() {
    secretKey = secretKeyService.getKey(yamlConfig);
  }

  @Override
  public AccessToken generateToken(Authentication authentication) {
    if (authentication.getPrincipal() instanceof UserDetails) {
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      long expireTime = getExpireTime(new Date());
      String accessToken = generateToken(new HashMap<>(), userPrincipal, expireTime);
      return new AccessToken(accessToken, "refreshToken", expireTime);
    } else {
      throw new IllegalArgumentException("Authentication Invalid.");
    }
  }

  @Override
  public String extractToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claim) {
    return claim.apply(extracAllClaims(token));
  }

  private Claims extracAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  @Override
  public boolean isValidationToken(String token, UserPrincipal userPrincipal) {
    String email = extractToken(token);
    return email.equals(userPrincipal.getEmail());
  }

  private String generateToken(Map<String, Object> claims, UserPrincipal userPrincipal, long expireTime) {
    claims.put("city", userPrincipal.getCity());
    claims.put("zipCode", userPrincipal.getZipCode());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userPrincipal.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(expireTime))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  private long getExpireTime(Date current) {
    return current.getTime() + yamlConfig.getJwt().getExpireTime() * 1000;
  }
}
