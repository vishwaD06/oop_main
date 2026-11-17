package com.sharesphere.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricingRecommendationRequest {
    private String name;
    private Long categoryId;
    private String condition; // New, Like New, Good, Fair
    private BigDecimal originalMrp;
}
