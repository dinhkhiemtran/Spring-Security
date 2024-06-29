package com.khiemtran.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = GreetingController.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class GreetingControllerTest {
  private final static String REQUEST_MAPPING = "/greeting";
  @InjectMocks
  private GreetingController greetingController;
  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(greetingController).build();
  }

  @Test
  public void getGreeting() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get(REQUEST_MAPPING))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().string("Hello you, Welcome to Spring Security."));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void getGreetingWithParam() {
    try {
      String s = REQUEST_MAPPING + "/{name}";
      mockMvc.perform(MockMvcRequestBuilders.get(s, "Alice"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().string("Hello Alice, Welcome to Spring Security."));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}