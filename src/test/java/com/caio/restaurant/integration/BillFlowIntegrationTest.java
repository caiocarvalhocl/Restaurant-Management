package com.caio.restaurant.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class BillFlowIntegrationTest extends AbstractIntegrationTest {

  private String loginAsOwner() throws Exception {
    return this.mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"email": "admin@restaurant.com", "password": "admin123"}\
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty())
        .andExpect(jsonPath("$.role").value("OWNER"))
        .andReturn()
        .getResponse()
        .getContentAsString()
        .replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
  }

  @Test
  void closingBillWithPixShouldMarkItClosedAndFreeTheTable() throws Exception {
    String token = loginAsOwner();

    Long categoryId =
        Long.valueOf(
            this.mockMvc
                .perform(
                    post("/api/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            """
                            {"name": "Test Category", "description": "Test Description"}\
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":(\\d+).*", "$1"));

    Long productId =
        Long.valueOf(
            this.mockMvc
                .perform(
                    post("/api/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            """
                            {"name": "Test Product", "description": "Test Product Description", "price": 10.0, "priceType": "FIXED", "available": true, "categoryId":\
                            """
                                + categoryId
                                + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Product Description"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.priceType").value("FIXED"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.category.id").value(categoryId))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":(\\d+).*", "$1"));

    Long tableId =
        Long.valueOf(
            this.mockMvc
                .perform(
                    post("/api/tables")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            """
                            {"tableNumber": 100, "capacity": 4, "status": "AVAILABLE"}\
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tableNumber").value(100))
                .andExpect(jsonPath("$.capacity").value(4))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":(\\d+).*", "$1"));

    Long billId =
        Long.valueOf(
            this.mockMvc
                .perform(
                    post("/api/bills")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            """
                            {"tableId":\
                            """
                                + tableId
                                + """
                                , "clientName": "Test Client"}\
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tableNumber").value(100))
                .andExpect(jsonPath("$.clientName").value("Test Client"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":(\\d+).*", "$1"));

    this.mockMvc
        .perform(
            post("/api/bills/" + billId + "/items")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"productId":\
                    """
                        + productId
                        + """
                        , "quantity": 2, "notes": "Test notes"}\
                        """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.items[0].productName").value("Test Product"))
        .andExpect(jsonPath("$.items[0].quantity").value(2))
        .andExpect(jsonPath("$.items[0].notes").value("Test notes"));

    this.mockMvc
        .perform(
            post("/api/bills/" + billId + "/close")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"paymentMethod": "PIX"}\
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(billId))
        .andExpect(jsonPath("$.status").value("CLOSED"))
        .andExpect(jsonPath("$.total").value(20.0));

    this.mockMvc
        .perform(get("/api/tables/" + tableId).header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(tableId))
        .andExpect(jsonPath("$.status").value("AVAILABLE"));
  }
}
