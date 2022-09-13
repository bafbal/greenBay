package com.bafbal.greenbay.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AccessControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void authenticate_WhenValidCredentialsGiven_ReturnsToken() throws Exception {
    String username = "foo";
    String password = "bar";
    String body = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}";
    mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.balance").exists())
        .andExpect(status().isOk());
  }

  @Test
  void authenticate_WhenUsernameIsMissing_DisplaysErrorMessage() throws Exception {
    String password = "bar";
    String body = "{\"password\": \"" + password + "\"}";
    mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Username is missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void authenticate_WhenPasswordIsMissing_DisplaysErrorMessage() throws Exception {
    String username = "foo";
    String body = "{\"username\": \"" + username + "\"}";
    mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Password is missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void authenticate_WhenUsernameAndPasswordIsMissing_DisplaysErrorMessage() throws Exception {
    String body = "{}";
    mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Username and password are missing.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void authenticate_WhenUsernameDoesNotExistInDB_DisplaysErrorMessage() throws Exception {
    String username = "fooo";
    String password = "bar";
    String body = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}";
    mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("No such user exists.")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void authenticate_WhenPasswordDoesNotMatch_DisplaysErrorMessage() throws Exception {
    String username = "foo";
    String password = "barr";
    String body = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}";
    mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(jsonPath("message", is("Wrong password.")))
        .andExpect(status().isBadRequest());
  }
}
