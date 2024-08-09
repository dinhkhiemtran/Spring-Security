package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;
import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessTokenResponse;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.exception.RoleNotFoundException;
import com.khiemtran.model.Role;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.service.AuthenticationService;
import com.khiemtran.service.JwtService;
import com.khiemtran.utils.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static com.khiemtran.model.RoleName.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private static final String REFRESH_TOKEN = "refresh_token";
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final YamlConfig yamlConfig;

  @Override
  public UserResponse register(SignUpRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailNotFoundException("User has already exist.");
    }
    Role roles = getRoles();
    User userEntity = new User(
        request.username(),
        passwordEncoder.encode(request.password()),
        request.email(),
        request.zipCode(),
        request.city(),
        Collections.singleton(roles));
    User user = userRepository.save(userEntity);
    return new UserResponse(user.getUsername(), user.getEmail(), user.getZipCode(), user.getCity());
  }

  @Override
  public AccessTokenResponse authenticate(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    UserPrincipal userPrincipal;
    if (authentication.getPrincipal() instanceof UserDetails) {
      userPrincipal = (UserPrincipal) authentication.getPrincipal();
    } else {
      throw new IllegalArgumentException("Authentication Invalid.");
    }
    long expireTime = getExpireTime(new Date());
    String accessToken = jwtService.generateToken(
        userPrincipal,
        TokenType.ACCESS_TOKEN,
        getExpireTime(new Date()));
    String refreshToken = jwtService.generateRefreshToken(
        userPrincipal,
        TokenType.REFRESH_TOKEN,
        getExpireDay(new Date()));
    return new AccessTokenResponse(accessToken, refreshToken, expireTime);
  }

  @Override
  public AccessTokenResponse refresh(HttpServletRequest request) {
    String refreshToken = Optional.ofNullable(request.getHeader(REFRESH_TOKEN))
        .orElseThrow(() -> new IllegalArgumentException("Refresh token is required."));
    String email = jwtService.extractToken(refreshToken, TokenType.REFRESH_TOKEN);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException("Not found email: " + email, HttpStatus.UNAUTHORIZED));
    UserPrincipal userPrincipal = new UserPrincipal(user);
    if (!(jwtService.isValidationToken(refreshToken, TokenType.REFRESH_TOKEN, userPrincipal))) {
      throw new IllegalArgumentException("Refresh token invalid.");
    }
    long expireTime = getExpireTime(new Date());
    String accessToken = jwtService.generateToken(userPrincipal, TokenType.ACCESS_TOKEN, expireTime);
    return new AccessTokenResponse(accessToken, refreshToken, expireTime);
  }

  private long getExpireTime(Date current) {
    return current.getTime() + 1000 * 60 * yamlConfig.getJwt().getExpireTime();
  }

  private long getExpireDay(Date current) {
    return current.getTime() + 1000 * 60 * 60 * 24 * yamlConfig.getJwt().getExpireDay();
  }

  private Role getRoles() {
    return roleRepository.findByName(ROLE_ADMIN)
        .orElseThrow(() -> new RoleNotFoundException("User's Role not set."));
  }
}
