package com.caio.restaurant.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

  @ServiceConnection
  protected static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:16-alpine");

  static {
    postgreSQLContainer.start();
  }

  @DynamicPropertySource
  static void jwtProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "jwt.secret",
        () ->
            "xRwhdI7oC38JZnKDScosNU6aGihzXPRu9bVIguabopzrVcvSqicWEt8HRPOhHAkBP51pDek6Ob0JfKongC000g==");
  }

  @Autowired protected MockMvc mockMvc;
}
