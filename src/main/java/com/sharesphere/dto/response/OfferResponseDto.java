package com.sharesphere.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OfferResponseDto {
    private Long id;
    private Long requestId;
    private UserResponse offerer;
    private String message;
    private BigDecimal priceOffered;
    private ItemResponse linkedItem;
    private LocalDateTime createdAt;
}
