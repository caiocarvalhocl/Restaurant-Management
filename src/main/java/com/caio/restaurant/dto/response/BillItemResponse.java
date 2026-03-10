package com.caio.restaurant.dto.response;

import com.caio.restaurant.entity.BillItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BillItemResponse(
    Long id,
    String productName,
    BigDecimal quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    String notes,
    LocalDateTime createdAt) {
  public static BillItemResponse toResponse(BillItem item) {
    return new BillItemResponse(
        item.getId(),
        item.getProduct().getName(),
        item.getQuantity(),
        item.getUnitPrice(),
        item.getTotalPrice(),
        item.getNotes(),
        item.getCreatedAt());
  }
}
