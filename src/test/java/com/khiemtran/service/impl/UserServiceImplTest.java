package com.khiemtran.service.impl;

import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.exception.RoleNotFoundException;
import com.khiemtran.model.Role;
import com.khiemtran.model.RoleName;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.security.provider.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ContextConfiguration(classes = UserServiceImpl.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
  @InjectMocks
  private UserServiceImpl userService;
  @MockBean
  private UserRepository userRepository;
  @MockBean
  private PasswordEncoder passwordEncoder;
  @MockBean
  private RoleRepository roleRepository;
  @MockBean
  private JwtProvider jwtProvider;
  @MockBean
  private AuthenticationManager authenticationManager;
  private SignUpRequest signUpRequest;
  private Role role;
  private User user;
  private UserResponse userResponse;
  private UserRequest userRequest;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    ReflectionTestUtils.setField(userService, "roleRepository", roleRepository);
    ReflectionTestUtils.setField(userService, "jwtProvider", jwtProvider);
    ReflectionTestUtils.setField(userService, "authenticationManager", authenticationManager);
    signUpRequest = new SignUpRequest("username", "password", "email@mail.com",
        "zipCode", "city");
    role = new Role();
    role.setId(1L);
    role.setName(RoleName.ROLE_ADMIN);
    user = new User(
        signUpRequest.username(),
        signUpRequest.password(),
        signUpRequest.email(),
        signUpRequest.zipCode(),
        signUpRequest.city(),
        Set.of(role));
    userResponse = new UserResponse(user.getUsername(), user.getEmail(), user.getZipCode(), user.getCity());
    userRequest = new UserRequest(signUpRequest.username(), signUpRequest.password(), signUpRequest.email(), signUpRequest.zipCode(), signUpRequest.city());
  }

  @Test
  public void createIllegalArgumentException() {
    Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(true);
    EmailNotFoundException emailNotFoundException = Assertions.assertThrows(EmailNotFoundException.class, () -> {
      userService.create(signUpRequest);
    });
    Assertions.assertEquals("User has already exist.", emailNotFoundException.getMessage());
  }

  @Test
  public void createRoleNotFoundException() {
    Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
    Mockito.when(roleRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());
    RoleNotFoundException roleNotFoundException =
        Assertions.assertThrows(RoleNotFoundException.class,
            () -> userService.create(signUpRequest));
    Assertions.assertEquals("User's Role not set.", roleNotFoundException.getMessage());
  }

  @Test
  public void create() {
    Mockito.when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
    Mockito.when(roleRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(role));
    Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
    UserResponse actual = userService.create(signUpRequest);
    Assertions.assertEquals(actual.username(), userResponse.username());
    Assertions.assertEquals(actual.city(), userResponse.city());
    Assertions.assertEquals(actual.email(), userResponse.email());
    Assertions.assertEquals(actual.zipCode(), userResponse.zipCode());
    Assertions.assertEquals(actual, userResponse);
  }

  @Test
  public void getUserEmpty() {
    Mockito.when(userRepository.findAll()).thenReturn(List.of());
    List<UserResponse> actual = userService.getAllUsers();
    Assertions.assertEquals(actual, List.of());
  }

  @Test
  public void getAllUser() {
    User user1 = new User("test", "test", "test@mail.com", "test", "tes", Set.of(role));
    Mockito.when(userRepository.findAll()).thenReturn(List.of(user, user1));
    List<UserResponse> actual = userService.getAllUsers();
    Assertions.assertEquals(2, actual.size());
    Assertions.assertEquals(userResponse, actual.getFirst());
    Assertions.assertEquals(userResponse.username(), actual.getFirst().username());
    Assertions.assertEquals(userResponse.zipCode(), actual.getFirst().zipCode());
    Assertions.assertEquals(userResponse.email(), actual.getFirst().email());
    Assertions.assertEquals(userResponse.city(), actual.getFirst().city());
  }

  @Test
  public void updateUserNotFoundWithEmail() {
    Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
        .thenReturn(Optional.empty());
    EmailNotFoundException emailNotFoundException = Assertions.assertThrows(EmailNotFoundException.class, () -> userService.updateUser(userRequest.email(), userRequest));
    Assertions.assertEquals("User not found with email: " + userRequest.email(), emailNotFoundException.getMessage());
  }

  @Test
  public void update() {
    Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(user));
    Mockito.when(userRepository.save(user)).thenReturn(user);
    Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn(userRequest.password());
    userService.updateUser(userRequest.email(), userRequest);
    Assertions.assertEquals(userRequest.username(), user.getUsername());
    Assertions.assertEquals(userRequest.password(), user.getPassword());
    Assertions.assertEquals(userRequest.username(), user.getUsername());
    Assertions.assertEquals(userRequest.username(), user.getUsername());
  }

  @Test
  public void removeEmailNoFoundException() {
    Mockito.when(userRepository.findByEmail(signUpRequest.email())).thenReturn(Optional.empty());
    EmailNotFoundException emailNotFoundException = Assertions.assertThrows(EmailNotFoundException.class, () -> userService.remove(signUpRequest.email()));
    Assertions.assertEquals("User not found with email: " + signUpRequest.email(), emailNotFoundException.getMessage());
  }

  @Test
  public void removeUser() {
    Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
    userService.remove(signUpRequest.email());
    Assertions.assertEquals(signUpRequest.email(), userRequest.email());
  }

  @Test
  public void getAccessToken() {
    LoginRequest loginRequest = new LoginRequest("email@mail.com", "password");
    AccessToken expect = new AccessToken("accessToken", 500L);
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signUpRequest.email(), signUpRequest.password());
    Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(usernamePasswordAuthenticationToken);
    Mockito.when(jwtProvider.generateToken(ArgumentMatchers.any())).thenReturn(expect);
    AccessToken actual = userService.getAccessToken(loginRequest);
    Assertions.assertEquals(expect.accessToken(), actual.accessToken());
    Assertions.assertEquals(expect.expiryDate(), actual.expiryDate());
  }
}