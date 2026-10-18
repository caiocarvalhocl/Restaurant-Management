package com.caio.restaurant.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class AuthIntegrationTest extends AbstractIntegrationTest {

  private void registerUser(String name, String email, String password, String role)
      throws Exception {
    if (this.mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        String.format(
                            """
                            {"email": "%s", "password": "%s"}\
                            """,
                            email, password)))
            .andReturn()
            .getResponse()
            .getStatus()
        == 200) {
      return;
    }

    this.mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    String.format(
                        """
                        {"name": "%s", "email": "%s", "password": "%s", "role": "%s"}\
                        """,
                        name, email, password, role)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").isNotEmpty())
        .andExpect(jsonPath("$.role").value(role));
  }

  @Test
  void loginWithValidCredentialsShouldReturnToken() throws Exception {
    this.mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"email": "admin@restaurant.com", "password": "admin123"}\
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty())
        .andExpect(jsonPath("$.role").value("OWNER"));
  }

  @Test
  void loginWithInvalidPasswordShouldReturnUnauthorized() throws Exception {
    this.mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"email": "admin@restaurant.com", "password": "wrongpassword"}\
                    """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void routesThatRequireAuthenticationShouldReturnUnauthorizedWhenNoTokenIsProvided()
      throws Exception {
    this.mockMvc
        .perform(
            post("/api/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"tableNumber": 1, "items": [{"name": "Pizza", "price": 10.0}]}\
                    """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Disabled
  // FIX: Implement token validation logic in the authentication filter to return
  // 401 Unauthorized for invalid tokens.
  void routesThatRequireAuthenticationShouldReturnUnauthorizedWhenInvalidTokenIsProvided()
      throws Exception {
    this.mockMvc
        .perform(
            post("/api/bills")
                .header("Authorization", "Bearer invalidtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"tableNumber": 1, "items": [{"name": "Pizza", "price": 10.0}]}\
                    """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void exclusiveRoutesForOwnerShouldReturnForbiddenWhenUserIsNotOwner() throws Exception {
    this.registerUser("bruno", "bruno@gmail.com", "bruno123", "EMPLOYEE");

    String token =
        this.mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {"email": "bruno@gmail.com", "password": "bruno123"}\
                        """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.role").value("EMPLOYEE"))
            .andReturn()
            .getResponse()
            .getContentAsString()
            .replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");

    this.mockMvc
        .perform(get("/api/bills").header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }
}
