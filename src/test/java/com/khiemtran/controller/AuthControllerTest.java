package com.khiemtran.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = AuthController.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
  private static final String BASE_PATH = "/api/v1";
  @InjectMocks
  private AuthController authController;
  @MockBean
  private UserService userService;
  private MockMvc mockMvc;
  private SignUpRequest signUpRequest;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    ReflectionTestUtils.setField(authController, "userService", userService);
    signUpRequest = new SignUpRequest("username", "password",
        "email@mail.com", "12345", "city");
  }

  @Test
  public void signup() {
    UserResponse userResponse = new UserResponse("username",
        "email@mail.com", "zipcode", "city");
    Mockito.when(userService.create(ArgumentMatchers.any())).thenReturn(userResponse);
    try {
      mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/sign-up")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(signUpRequest)))
          .andExpect(MockMvcResultMatchers.status().isCreated())
          .andExpect(MockMvcResultMatchers.header().stringValues("Location", "http://localhost:8080/api/v1/username"))
          .andExpect(MockMvcResultMatchers.content().string("User is created successfully"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void login() {
    AccessToken accessToken = new AccessToken("accessToken", 500L);
    Mockito.when(userService.getAccessToken(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(accessToken);
    try {
      mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/login")
              .param("email", "email@mail.com")
              .param("password", "12345")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(accessToken)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}