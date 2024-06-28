package com.khiemtran.security.provider;

import com.khiemtran.config.JwtConfig;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.security.model.UserPrincipal;
import com.khiemtran.utils.SecretKeySecretUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.application.jwt", name = "expire-time")
public class JwtProvider {
  private final SecretKeySecretUtil secretKeySecretUtil;
  private final JwtConfig jwtConfig;

  public AccessToken generateToken(Authentication authentication) {
    long expireTime = jwtConfig.getExpireTime() * 1000;
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    String accessToken
        = Jwts.builder()
        .setSubject(String.valueOf(principal.getId()))
        .setIssuer(principal.getEmail())
        .setExpiration(getExpireTime(expireTime))
        .setAudience(principal.getUsername())
        .signWith(secretKeySecretUtil.getKey(), SignatureAlgorithm.HS256)
        .compact();
    return new AccessToken(accessToken, expireTime);
  }

  private static Date getExpireTime(long expireTime) {
    Date now = new Date();
    long currentTime = now.getTime();
    return new Date(currentTime + expireTime);
  }
}
