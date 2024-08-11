package com.khiemtran.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessTokenResponse;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = AuthController.class)
@ActiveProfiles("test")
@SpringBootTest
class AuthControllerTest {
  private static final String BASE_PATH = "/auth";
  @InjectMocks
  private AuthController authController;
  @MockBean
  private AuthenticationService authenticationService;
  private MockMvc mockMvc;
  @Mock
  private SignUpRequest signUpRequest;
  private AccessTokenResponse accessToken;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(authController, "authenticationService", authenticationService);
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    signUpRequest = new SignUpRequest("username", "password",
        "email@mail.com", "12345", "city");
    accessToken = new AccessTokenResponse("accessToken", "refreshToken", 500L);
  }

  @Test
  public void signup() throws Exception {
    UserResponse userResponse = new UserResponse("username", "email@mail.com", "zipcode", "city");
    when(authenticationService.register(signUpRequest)).thenReturn(userResponse);
    mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(signUpRequest)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string("User is created successfully."))
        .andExpect(MockMvcResultMatchers.header().stringValues("Location", "http://localhost/auth/username"));
  }

  @Test
  public void login() {
    accessToken = new AccessTokenResponse("accessToken", "refreshToken", 500L);
    when(authenticationService.authenticate(any())).thenReturn(accessToken);
    try {
      mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/login")
              .header("email", "email@mail.com")
              .header("password", "12345")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(accessToken)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void refresh() {
    accessToken = new AccessTokenResponse("accessToken", "refreshToken", 500L);
    when(authenticationService.refresh(any()))
        .thenReturn(accessToken);
    try {
      mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/refresh")
              .header("refresh_token", "refreshToken")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(accessToken)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void logout() {
    accessToken = new AccessTokenResponse("accessToken", "refreshToken", 500L);
    Mockito.doNothing().when(authenticationService).logout(Mockito.anyString());
    try {
      mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/logout")
              .header("access_token", "accessToken")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().string("User has successfully signed out."));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}