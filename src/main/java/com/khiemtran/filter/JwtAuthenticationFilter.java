package com.khiemtran.filter;

import com.khiemtran.constants.TokenType;
import com.khiemtran.service.JwtService;
import com.khiemtran.service.TokenService;
import com.khiemtran.service.impl.UserDetailsServiceImp;
import com.khiemtran.utils.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Setter
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsServiceImp userDetailsServiceImp;
  private final TokenService tokenService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String bearerToken = request.getHeader("Authorization");
    if (isBearerTokenValid(bearerToken)) {
      String token = extractToken(bearerToken);
      String email = jwtService.extractToken(token, TokenType.ACCESS_TOKEN);
      if (isTokenLoggedOutOrUnauthenticated(email)) {
        filterChain.doFilter(request, response);
        return;
      }
      authenticateRequest(request, token, email);
    }
    filterChain.doFilter(request, response);
  }

  private boolean isBearerTokenValid(String bearerToken) {
    return StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer");
  }

  private String extractToken(String bearerToken) {
    return bearerToken.substring(7);
  }

  private boolean isTokenLoggedOutOrUnauthenticated(String email) {
    return tokenService.isTokenLoggedOut(email) || SecurityContextHolder.getContext().getAuthentication() != null;
  }

  private void authenticateRequest(HttpServletRequest request, String token, String email) {
    if (StringUtils.isNotBlank(email)) {
      UserPrincipal userPrincipal = (UserPrincipal) userDetailsServiceImp.loadUserByUsername(email);
      if (jwtService.isValidationToken(token, TokenType.ACCESS_TOKEN, userPrincipal)) {
        setAuthenticationContext(request, userPrincipal);
      }
    }
  }

  private void setAuthenticationContext(HttpServletRequest request, UserPrincipal userPrincipal) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}