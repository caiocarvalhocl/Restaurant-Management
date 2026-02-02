package com.caio.restaurant.dto.request;

import com.caio.restaurant.enums.TableStatus;

import jakarta.validation.constraints.NotNull;

public record RestaurantTableRequest(@NotNull Integer tableNumber, @NotNull Integer capacity,
                TableStatus status) {

}
