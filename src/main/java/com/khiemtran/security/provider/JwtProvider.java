package com.khiemtran.security.provider;

import com.khiemtran.security.model.UserPrincipal;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.utils.SecretKeySecretUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
  private static final long EXPIRE_TIME = System.currentTimeMillis() + 1000 * 60 * 60;
  private final Date EXPIRY_DATE = new Date(EXPIRE_TIME);
  private final SecretKeySecretUtil secretKeySecretUtil;

  public AccessToken generateToken(Authentication authentication) {
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    String accessToken
        = Jwts.builder()
        .setSubject(String.valueOf(principal.getId()))
        .setIssuer(principal.getEmail())
        .setExpiration(EXPIRY_DATE)
        .setAudience(principal.getUsername())
        .signWith(secretKeySecretUtil.getKey(), SignatureAlgorithm.HS256)
        .compact();
    return new AccessToken(accessToken, EXPIRE_TIME);
  }
}
