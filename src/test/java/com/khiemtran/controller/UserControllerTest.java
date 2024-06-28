package com.khiemtran.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khiemtran.controller.UserController;
import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.List;

@ContextConfiguration(classes = UserController.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserControllerTest {
  private static final String BASE_PATH = "/api/v1";
  @InjectMocks
  private UserController userController;
  @MockBean
  private UserService userService;
  private MockMvc mockMvc;
  private UserRequest userRequest;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    ReflectionTestUtils.setField(userController, "userService", userService);
    userRequest = new UserRequest("username", "password",
        "email@mail.com", "zipCode", "city");
  }

  @Test
  public void getAllUsers() {
    UserResponse userResponse = new UserResponse("test", "test@mail.com", "test", "test");
    Mockito.when(userService.getAllUsers()).thenReturn(List.of(userResponse));
    try {
      mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/user"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content()
              .string(new ObjectMapper().writeValueAsString(List.of(userResponse))));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void updateUser() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.put(BASE_PATH + "/user")
              .param("email", userRequest.email())
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(userRequest)))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().string("User is updated successfully."));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void deleteUser() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/user/email@mail.com")
              .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().string("User is removed successfully."));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}