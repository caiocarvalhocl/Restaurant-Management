package com.caio.restaurant.dto.response;

import java.time.LocalDateTime;

import com.caio.restaurant.dto.request.RestaurantTableRequest;
import com.caio.restaurant.entity.RestaurantTable;
import com.caio.restaurant.enums.TableStatus;

public record RestaurantTableResponse(Long id, Integer tableNumber, Integer capacity, TableStatus status,
    LocalDateTime createdAt) {

  public static RestaurantTableResponse toResponse(RestaurantTable table) {
    return new RestaurantTableResponse(
        table.getId(),
        table.getTableNumber(),
        table.getCapacity(),
        table.getStatus(),
        table.getCreatedAt());
  }

  public static RestaurantTable toEntity(RestaurantTableRequest request) {
    RestaurantTable table = new RestaurantTable();
    table.setTableNumber(request.tableNumber());
    table.setCapacity(request.capacity());
    table.setStatus(request.status());
    return table;
  }
}
