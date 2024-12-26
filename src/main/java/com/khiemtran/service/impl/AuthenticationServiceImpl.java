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
import com.khiemtran.model.Token;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.service.AuthenticationService;
import com.khiemtran.service.JwtService;
import com.khiemtran.service.TokenService;
import com.khiemtran.utils.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static com.khiemtran.constants.TokenType.ACCESS_TOKEN;
import static com.khiemtran.model.RoleName.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
  private static final String REFRESH_TOKEN = "refresh_token";
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final YamlConfig yamlConfig;
  private final TokenService tokenService;

  @Override
  public UserResponse register(SignUpRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailNotFoundException("User already exists.");
    }
    Role roles = getRoles();
    User user = userRepository.save(new User(
        request.username(),
        passwordEncoder.encode(request.password()),
        request.email(),
        request.zipCode(),
        request.city(),
        Collections.singleton(roles)));
    log.info("User successfully registered with username: {} and email: {}", user.getUsername(), user.getEmail());
    return new UserResponse(user.getUsername(), user.getEmail(), user.getZipCode(), user.getCity());
  }

  @Override
  public AccessTokenResponse authenticate(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    long expireTime = getExpireTime(new Date());
    String accessToken = jwtService.generateToken(userPrincipal, ACCESS_TOKEN, expireTime);
    String refreshToken = jwtService.generateRefreshToken(userPrincipal, TokenType.REFRESH_TOKEN, getExpireDay(new Date()));
    tokenService.save(new Token(accessToken, refreshToken, userPrincipal.getEmail()));
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
    if (!jwtService.isValidationToken(refreshToken, TokenType.REFRESH_TOKEN, userPrincipal)) {
      throw new IllegalArgumentException("Refresh token invalid.");
    }
    long expireTime = getExpireTime(new Date());
    String accessToken = jwtService.generateToken(userPrincipal, ACCESS_TOKEN, expireTime);
    tokenService.save(new Token(accessToken, refreshToken, userPrincipal.getEmail()));
    return new AccessTokenResponse(accessToken, refreshToken, expireTime);
  }

  @Override
  public void logout(String accessToken) {
    String email = jwtService.extractToken(accessToken, ACCESS_TOKEN);
    tokenService.delete(email);
  }

  private long getExpireTime(Date current) {
    return current.getTime() + 1000L * 60 * yamlConfig.getJwt().getExpireTime();
  }

  private long getExpireDay(Date current) {
    return current.getTime() + 1000L * 60 * 60 * 24 * yamlConfig.getJwt().getExpireDay();
  }

  private Role getRoles() {
    return roleRepository.findByName(ROLE_ADMIN)
        .orElseThrow(() -> new RoleNotFoundException("User's Role not set."));
  }
}