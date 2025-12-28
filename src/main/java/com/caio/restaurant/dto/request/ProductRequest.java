package com.caio.restaurant.dto.request;

import java.math.BigDecimal;

import com.caio.restaurant.enums.PriceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequest(@NotBlank @Size(max = 100, message = "Name must be less than 100 characters") String name,
                                                                @Size(max = 255, message = "Description must be less than 255 characters") String description,
                                                                @NotNull @Positive BigDecimal price,
                                                                @NotNull PriceType priceType, Boolean available,
                                                                @NotNull Long categoryId) {
}
