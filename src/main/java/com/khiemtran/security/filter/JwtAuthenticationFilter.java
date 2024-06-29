package com.khiemtran.security.filter;

import com.khiemtran.security.service.UserDetailsServiceImp;
import com.khiemtran.utils.SecretKeySecretUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Setter
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final UserDetailsServiceImp userDetailsServiceImp;
  private final SecretKeySecretUtil secretKeySecretUtil;

  @SuppressWarnings("NullableProblems")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
      String jwt = bearerToken.substring(7);
      Jws<Claims> claimsJws = getClaims(jwt);
      if (claimsJws != null) {
        Claims body = claimsJws.getBody();
        UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(body.getSubject());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
      }
    }
    filterChain.doFilter(request, response);
  }

  public Jws<Claims> getClaims(String jwt) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKeySecretUtil.getKey())
          .build()
          .parseClaimsJws(jwt);
    } catch (SignatureException ex) {
      logger.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty.");
    }
    return null;
  }
}
