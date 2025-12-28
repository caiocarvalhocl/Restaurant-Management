package com.caio.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
    @NotBlank(message = "Name is required") @Size(max = 50, message = "Name must be less than 50 characters") String name,
    @Size(max = 255, message = "Description must be less than 255 characters") String description) {
}
