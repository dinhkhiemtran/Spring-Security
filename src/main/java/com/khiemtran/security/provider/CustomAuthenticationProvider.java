package com.khiemtran.security.provider;

import com.khiemtran.security.service.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
  private final UserDetailsServiceImp userDetailsServiceImp;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails principal = userDetailsServiceImp.loadUserByEmail(authentication.getName());
    return
        UsernamePasswordAuthenticationToken
            .authenticated(principal
                , authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(principal.getAuthorities()));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
