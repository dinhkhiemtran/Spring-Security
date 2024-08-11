package com.khiemtran.service.impl;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.model.Role;
import com.khiemtran.model.RoleName;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.service.JwtService;
import com.khiemtran.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = AuthenticationServiceImpl.class)
@ActiveProfiles("test")
@SpringBootTest
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
  private SignUpRequest request;
  private UserResponse userResponse;
  private Role role;
  private User user;

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
  }

  @Test
  public void registerWhenUserExists() {
    when(userRepository.existsByEmail(anyString())).thenReturn(true);
    EmailNotFoundException emailNotFoundException = Assertions.assertThrows(EmailNotFoundException.class, () ->
        authenticationService.register(request));
    Assertions.assertEquals("User already exists.", emailNotFoundException.getMessage());
  }

  @Test
  public void userRegisterSuccessful() {
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.ofNullable(role));
    when(userRepository.save(any())).thenReturn(user);
    UserResponse actual = authenticationService.register(request);
    Assertions.assertEquals(actual, userResponse);
    Assertions.assertEquals(actual.username(), userResponse.username());
    Assertions.assertEquals(actual.email(), userResponse.email());
    Assertions.assertEquals(actual.zipCode(), userResponse.zipCode());
    Assertions.assertEquals(actual.city(), userResponse.city());
  }
}