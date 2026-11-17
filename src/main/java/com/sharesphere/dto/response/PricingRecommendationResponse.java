package com.sharesphere.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PricingRecommendationResponse {
    private BigDecimal suggestedSellPrice;
    private BigDecimal suggestedRentalPerDay;
    private BigDecimal suggestedSecurityDeposit;
    private BigDecimal estimatedMrp;
    private double depreciationRatePercent;
    private double surgeMultiplier;
    private String demandLevel; // NORMAL, HIGH, PEAK_EXAM_SURGE
    private String spikeReason;
    private String explanation;
}
