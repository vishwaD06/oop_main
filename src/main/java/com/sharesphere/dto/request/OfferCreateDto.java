package com.sharesphere.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferCreateDto {
    @NotBlank(message = "Offer message is required")
    private String message;

    private BigDecimal priceOffered;
    private Long linkedItemId;
}
