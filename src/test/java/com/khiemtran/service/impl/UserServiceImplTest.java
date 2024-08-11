package com.khiemtran.service.impl;

import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.model.User;
import com.khiemtran.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = UserServiceImpl.class)
@ActiveProfiles("test")
@SpringBootTest
class UserServiceImplTest {
  @InjectMocks
  private UserServiceImpl userService;
  @MockBean
  private UserRepository userRepository;
  @MockBean
  private PasswordEncoder passwordEncoder;
  private User user;
  private UserResponse userResponse;
  private UserRequest userRequest;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    user = new User("username", "password", "email@mail", "12345", "city");
    userResponse = new UserResponse("username", "email@mail", "12345", "city");
    userRequest = new UserRequest("username", "password", "email@mail", "12345", "city");
  }

  @Test
  public void getAllUsers() {
    when(userRepository.findAll()).thenReturn(List.of(user));
    List<UserResponse> userResponses = userService.getAllUsers();
    Assertions.assertEquals(List.of(userResponse), userResponses);
  }

  @Test
  public void updateUserNotFoundWithEmail() {
    when(userRepository.findByUsername(anyString()))
        .thenReturn(Optional.empty());
    EmailNotFoundException emailNotFoundException = Assertions.assertThrows(EmailNotFoundException.class, () -> userService.updateUser(userRequest.email(), userRequest));
    Assertions.assertEquals("User not found with email: " + userRequest.email(), emailNotFoundException.getMessage());
  }

  @Test
  public void update() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
    when(userRepository.save(user)).thenReturn(user);
    when(passwordEncoder.encode(anyString())).thenReturn(userRequest.password());
    userService.updateUser(userRequest.email(), userRequest);
    Assertions.assertEquals(userRequest.username(), user.getUsername());
    Assertions.assertEquals(userRequest.password(), user.getPassword());
    Assertions.assertEquals(userRequest.username(), user.getUsername());
    Assertions.assertEquals(userRequest.username(), user.getUsername());
  }

  @Test
  public void removeEmailNoFoundException() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    EmailNotFoundException emailNotFoundException =
        Assertions.assertThrows(EmailNotFoundException.class, () -> userService.remove("test@mail"));
    Assertions.assertEquals("User not found with email: " + "test@mail", emailNotFoundException.getMessage());
  }

  @Test
  public void removeUser() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
    userService.remove(user.getEmail());
    Assertions.assertEquals("email@mail", userRequest.email());
  }
}