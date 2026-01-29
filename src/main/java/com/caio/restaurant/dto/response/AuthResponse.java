package com.caio.restaurant.dto.response;

import com.caio.restaurant.enums.UserRole;

public record AuthResponse(String token, String type, Long id, String name, String email, UserRole role) {
  public AuthResponse(String token, Long id, String name, String email, UserRole role) {
    this(token, "Bearer", id, name, email, role);
  }
}
