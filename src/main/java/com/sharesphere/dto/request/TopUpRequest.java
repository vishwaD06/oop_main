package com.sharesphere.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopUpRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "10.00", message = "Minimum top-up is 10.00")
    private BigDecimal amount;
}
