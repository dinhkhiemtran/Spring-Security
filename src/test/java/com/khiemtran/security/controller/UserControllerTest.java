package com.khiemtran.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khiemtran.controller.UserController;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
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
  private static final String REQUEST_MAPPING = "/api/v1";
  @InjectMocks
  private UserController userController;
  @MockBean
  private UserService userService;
  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    ReflectionTestUtils.setField(userController, "userService", userService);
  }

  @Test
  public void getUser() {
    UserResponse userResponse = new UserResponse("test", "test@mail.com", "test", "test");
    Mockito.when(userService.getAllUsers()).thenReturn(List.of(userResponse));
    try {
      mockMvc.perform(MockMvcRequestBuilders.get(REQUEST_MAPPING + "/user"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content()
              .string(new ObjectMapper().writeValueAsString(List.of(userResponse))));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}