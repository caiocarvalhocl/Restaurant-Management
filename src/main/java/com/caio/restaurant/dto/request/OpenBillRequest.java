package com.caio.restaurant.dto.request;

public record OpenBillRequest(
        Long tableId, // nullable for takeaway
        String clientName // optional
) {
}
