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
import org.springframework.security.core.context.SecurityContext;
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

  @SuppressWarnings("NullableProblems")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("******************Pre-Filter******************");
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer")) {
      String token = bearerToken.substring(7);
      String email = jwtService.extractToken(token, TokenType.ACCESS_TOKEN);
      // Proceed with the filter chain if the token is logged out
      if (tokenService.isTokenLoggedOut(email)) {
        filterChain.doFilter(request, response);
        return;
      }
      if (StringUtils.isNotBlank(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserPrincipal userPrincipal = (UserPrincipal) userDetailsServiceImp.loadUserByUsername(email);
        if (jwtService.isValidationToken(token, TokenType.ACCESS_TOKEN, userPrincipal)) {
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          context.setAuthentication(authentication);
          SecurityContextHolder.setContext(context);
          log.info("Authentication Details: " + authentication.getDetails());
        }
      }
    }
    filterChain.doFilter(request, response);
  }
}