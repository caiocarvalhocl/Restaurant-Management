package com.caio.restaurant.dto.response;

import com.caio.restaurant.entity.Bill;
import com.caio.restaurant.enums.BillStatus;
import com.caio.restaurant.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BillResponse(
        Long id,
        Integer tableNumber,
        String clientName,
        BillStatus status,
        String openedByName,
        String closedByName,
        PaymentMethod paymentMethod,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal total,
        LocalDateTime openedAt,
        LocalDateTime closedAt,
        List<BillItemResponse> items) {

    public static BillResponse toResponse(Bill bill) {
        return new BillResponse(
                bill.getId(),
                bill.getTable() != null ? bill.getTable().getTableNumber() : null,
                bill.getClientName(),
                bill.getStatus(),
                bill.getOpenedBy().getName(),
                bill.getClosedBy() != null ? bill.getClosedBy().getName() : null,
                bill.getPaymentMethod(),
                bill.getSubtotal(),
                bill.getDiscount(),
                bill.getTotal(),
                bill.getOpenedAt(),
                bill.getClosedAt(),
                bill.getItems().stream().map(BillItemResponse::toResponse).toList());
    }
}
