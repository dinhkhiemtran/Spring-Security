package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;
import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessTokenResponse;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.model.Role;
import com.khiemtran.model.RoleName;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.service.JwtService;
import com.khiemtran.service.TokenService;
import com.khiemtran.utils.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = AuthenticationServiceImpl.class)
@ActiveProfiles("test")
@SpringBootTest
@Slf4j
class AuthenticationServiceImplTest {
  @InjectMocks
  private AuthenticationServiceImpl authenticationService;
  @MockBean
  private UserRepository userRepository;
  @MockBean
  private RoleRepository roleRepository;
  @MockBean
  private PasswordEncoder passwordEncoder;
  @MockBean
  private AuthenticationManager authenticationManager;
  @MockBean
  private JwtService jwtService;
  @MockBean
  private YamlConfig yamlConfig;
  @MockBean
  private TokenService tokenService;
  @Mock
  private Authentication authentication;
  @Mock
  private HttpServletRequest httpServletRequest;
  private SignUpRequest request;
  private UserResponse userResponse;
  private Role role;
  private User user;
  private LoginRequest loginRequest;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(authenticationService, "userRepository", userRepository);
    ReflectionTestUtils.setField(authenticationService, "roleRepository", roleRepository);
    ReflectionTestUtils.setField(authenticationService, "passwordEncoder", passwordEncoder);
    ReflectionTestUtils.setField(authenticationService, "authenticationManager", authenticationManager);
    ReflectionTestUtils.setField(authenticationService, "jwtService", jwtService);
    ReflectionTestUtils.setField(authenticationService, "yamlConfig", yamlConfig);
    ReflectionTestUtils.setField(authenticationService, "tokenService", tokenService);
    request = new SignUpRequest("username", "password", "email@mail", "12345", "city");
    userResponse = new UserResponse("username", "email@mail", "zipcode", "city");
    role = new Role(RoleName.ROLE_ADMIN);
    Set<Role> roles = new HashSet<>();
    roles.add(role);
    user = new User("username", "password", "email@mail", "zipcode", "city", roles);
    loginRequest = new LoginRequest("test", "test");
  }

  @Test
  public void registerWhenUserExists() {
    when(userRepository.existsByEmail(anyString())).thenReturn(true);
    EmailNotFoundException emailNotFoundException = Assertions.assertThrows(EmailNotFoundException.class, () ->
        authenticationService.register(request));
    assertEquals("User already exists.", emailNotFoundException.getMessage());
  }

  @Test
  public void userRegisterSuccessful() {
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.ofNullable(role));
    when(userRepository.save(any())).thenReturn(user);
    UserResponse actual = authenticationService.register(request);
    assertEquals(actual, userResponse);
    assertEquals(actual.username(), userResponse.username());
    assertEquals(actual.email(), userResponse.email());
    assertEquals(actual.zipCode(), userResponse.zipCode());
    assertEquals(actual.city(), userResponse.city());
  }

  @Test
  public void authenticatePrincipalNotInstanceOfUserDetails() {
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationService.authenticate(loginRequest));
    assertEquals("Authentication Invalid.", illegalArgumentException.getMessage());
  }

  @Test
  public void authenticate_Success() {
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    UserPrincipal userPrincipal = new UserPrincipal(user);
    when(authentication.getPrincipal()).thenReturn(userPrincipal);
    YamlConfig.Jwt jwt = new YamlConfig.Jwt();
    jwt.setExpireTime(300);
    jwt.setExpireDay(3);
    when(yamlConfig.getJwt()).thenReturn(jwt);
    String generatedAccessToken = "accessToken";
    String generatedRefreshToken = "refreshToken";
    when(jwtService.generateToken(any(UserPrincipal.class), eq(TokenType.ACCESS_TOKEN), anyLong()))
        .thenReturn(generatedAccessToken);
    when(jwtService.generateRefreshToken(any(UserPrincipal.class), eq(TokenType.REFRESH_TOKEN), anyLong()))
        .thenReturn(generatedRefreshToken);
    AccessTokenResponse actual = authenticationService.authenticate(loginRequest);
    assertNotNull(actual);
    assertEquals(generatedAccessToken, actual.accessToken());
    assertEquals(generatedRefreshToken, actual.refreshToken());
  }

  @Test
  public void refreshTokenNull() {
    when(httpServletRequest.getHeader("refresh_token")).thenReturn(null);
    IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class,
        () -> authenticationService.refresh(httpServletRequest));
    Assertions.assertEquals("Refresh token is required.", illegalArgumentException.getMessage());
  }

  @Test
  public void refreshTokenRepositoryNotFoundEmail() {
    when(httpServletRequest.getHeader("refresh_token")).thenReturn("refreshToken");
    when(jwtService.extractToken(anyString(), eq(TokenType.REFRESH_TOKEN))).thenReturn("email");
    EmailNotFoundException emailNotFoundException = Assertions.assertThrows(EmailNotFoundException.class,
        () -> authenticationService.refresh(httpServletRequest));
    Assertions.assertEquals("Not found email: email", emailNotFoundException.getMessage());
  }

  @Test
  public void refreshTokenNotValidationToken() {
    when(httpServletRequest.getHeader("refresh_token")).thenReturn("refreshToken");
    when(jwtService.extractToken(anyString(), eq(TokenType.REFRESH_TOKEN))).thenReturn("email");
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
    when(jwtService.isValidationToken(anyString(), eq(TokenType.REFRESH_TOKEN), any(UserPrincipal.class)))
        .thenReturn(false);
    IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class,
        () -> authenticationService.refresh(httpServletRequest));
    Assertions.assertEquals("Refresh token invalid.", illegalArgumentException.getMessage());
  }

  @Test
  public void refreshTokenSuccessfully() {
    when(httpServletRequest.getHeader("refresh_token")).thenReturn("refreshToken");
    when(jwtService.extractToken(anyString(), eq(TokenType.REFRESH_TOKEN))).thenReturn("email");
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
    when(jwtService.isValidationToken(anyString(), eq(TokenType.REFRESH_TOKEN), any(UserPrincipal.class)))
        .thenReturn(true);
    YamlConfig.Jwt jwt = new YamlConfig.Jwt();
    jwt.setExpireTime(300);
    jwt.setExpireDay(3);
    when(yamlConfig.getJwt()).thenReturn(jwt);
    String generatedAccessToken = "accessToken";
    String generatedRefreshToken = "refreshToken";
    when(jwtService.generateToken(any(UserPrincipal.class), eq(TokenType.ACCESS_TOKEN), anyLong()))
        .thenReturn(generatedAccessToken);
    when(jwtService.generateRefreshToken(any(UserPrincipal.class), eq(TokenType.REFRESH_TOKEN), anyLong()))
        .thenReturn(generatedRefreshToken);
    AccessTokenResponse actual = authenticationService.refresh(httpServletRequest);
    assertNotNull(actual);
    assertEquals(generatedAccessToken, actual.accessToken());
    assertEquals(generatedRefreshToken, actual.refreshToken());
  }

  @Test
  public void logout() {
    String accessToken = "dummyAccessToken";
    String email = "test@example.com";
    when(jwtService.extractToken(accessToken, TokenType.ACCESS_TOKEN)).thenReturn(email);
    authenticationService.logout(accessToken);
    verify(jwtService).extractToken(accessToken, TokenType.ACCESS_TOKEN);
    verify(tokenService).delete(email);
  }
}