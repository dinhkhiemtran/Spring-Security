package com.khiemtran.security.provider;

import com.khiemtran.config.JwtConfig;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.model.Role;
import com.khiemtran.security.model.UserPrincipal;
import com.khiemtran.utils.SecretKeySecretUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.application.jwt", name = "expire-time")
public class JwtProvider {
  private static final String AUD = "dinhkhiem";
  private final SecretKeySecretUtil secretKeySecretUtil;
  private final JwtConfig jwtConfig;

  public AccessToken generateToken(Authentication authentication) {
    Date now = new Date();
    long expireTime = now.getTime() + jwtConfig.getExpireTime() * 1000;
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    Map<String, CustomClaims> maps = new HashMap<>();
    CustomClaims claims = new CustomClaims(principal.getId(),
        principal.getZipCode(), principal.getCity(), principal.getRoles());
    maps.put("claims", claims);
    String accessToken = Jwts.builder()
        .setIssuer(principal.getUsername())
        .setClaims(maps)
        .setExpiration(new Date(expireTime))
        .setAudience(AUD)
        .setSubject(principal.getEmail())
        .signWith(secretKeySecretUtil.getKey(), SignatureAlgorithm.HS256)
        .compact();
    return new AccessToken(accessToken, expireTime);
  }
}

record CustomClaims(Long id, String zipCode, String city, Set<Role> roles) {
}

