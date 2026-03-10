package com.caio.restaurant.dto.request;

import com.caio.restaurant.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CloseBillRequest(
                @NotNull(message = "Payment method is required") PaymentMethod paymentMethod,

                BigDecimal discount // optional
) {
}
