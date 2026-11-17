package com.sharesphere.service;

import com.sharesphere.dto.request.PricingRecommendationRequest;
import com.sharesphere.dto.response.PricingRecommendationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DynamicPricingService {

    public PricingRecommendationResponse recommendPricing(PricingRecommendationRequest req) {
        String name = req.getName() != null ? req.getName().toLowerCase() : "";
        String condition = req.getCondition() != null ? req.getCondition() : "Good";
        Long catId = req.getCategoryId();

        // 1. Determine or estimate MRP
        BigDecimal mrp = req.getOriginalMrp();
        if (mrp == null || mrp.compareTo(BigDecimal.ZERO) <= 0) {
            mrp = estimateDefaultMrp(catId, name);
        }

        // 2. Condition Depreciation
        double depreciationRate = getDepreciationRate(condition);
        double retainedValueRate = 1.0 - depreciationRate; // e.g. 0.70 for Like New

        // 3. Demand Spike / Seasonality Detection
        double surgeMultiplier = 1.0;
        String demandLevel = "NORMAL";
        String spikeReason = "Standard campus demand";

        if ((catId != null && (catId == 3 || catId == 4)) || name.contains("calculator") || name.contains("drafter") || name.contains("fx-991")) {
            surgeMultiplier = 1.25;
            demandLevel = "PEAK_EXAM_SURGE";
            spikeReason = "🔥 Peak Exam Season Surge (+25% demand in Calculators & Academic Tools)";
        } else if ((catId != null && (catId == 7 || catId == 8)) || name.contains("cycle") || name.contains("table") || name.contains("kettle") || name.contains("cooler")) {
            surgeMultiplier = 1.15;
            demandLevel = "HIGH";
            spikeReason = "🚲 New Semester Hostel Move-In Surge (+15% demand for Cycles & Furniture)";
        } else if (name.contains("projector") || name.contains("speaker") || (catId != null && catId == 11)) {
            surgeMultiplier = 1.20;
            demandLevel = "HIGH";
            spikeReason = "🎤 Campus Event Season Demand (+20% for AV & Event Equipment)";
        }

        // 4. Calculate prices
        BigDecimal baseValue = mrp.multiply(BigDecimal.valueOf(retainedValueRate));
        BigDecimal sellPrice = baseValue.setScale(0, RoundingMode.HALF_UP);

        // Daily rental: ~2.5% to 3.5% of resale value, scaled by surge
        BigDecimal rentalPerDay = sellPrice.multiply(BigDecimal.valueOf(0.028 * surgeMultiplier))
                .setScale(0, RoundingMode.HALF_UP);
        if (rentalPerDay.compareTo(BigDecimal.valueOf(10)) < 0) {
            rentalPerDay = BigDecimal.valueOf(10);
        }

        // Security deposit: 30% to 50% of resale value (min 50)
        BigDecimal deposit = sellPrice.multiply(BigDecimal.valueOf(0.35))
                .setScale(0, RoundingMode.HALF_UP);
        if (deposit.compareTo(BigDecimal.valueOf(50)) < 0) {
            deposit = BigDecimal.valueOf(50);
        }

        String explanation = String.format(
                "Estimated from MRP ₹%s based on '%s' condition (retained %d%% value) with %s.",
                mrp.toPlainString(),
                condition,
                (int)(retainedValueRate * 100),
                spikeReason
        );

        return PricingRecommendationResponse.builder()
                .suggestedSellPrice(sellPrice)
                .suggestedRentalPerDay(rentalPerDay)
                .suggestedSecurityDeposit(deposit)
                .estimatedMrp(mrp)
                .depreciationRatePercent(depreciationRate * 100)
                .surgeMultiplier(surgeMultiplier)
                .demandLevel(demandLevel)
                .spikeReason(spikeReason)
                .explanation(explanation)
                .build();
    }

    private BigDecimal estimateDefaultMrp(Long catId, String name) {
        if (catId != null) {
            switch (catId.intValue()) {
                case 1: return BigDecimal.valueOf(8000);  // Electronics
                case 2: return BigDecimal.valueOf(1200);  // Books
                case 3: return BigDecimal.valueOf(1500);  // Calculators
                case 4: return BigDecimal.valueOf(1200);  // Engineering Tools
                case 5: return BigDecimal.valueOf(1800);  // Lab Equipment
                case 6: return BigDecimal.valueOf(2000);  // Sports
                case 7: return BigDecimal.valueOf(3500);  // Furniture
                case 8: return BigDecimal.valueOf(8000);  // Cycles
                case 9: return BigDecimal.valueOf(1500);  // Bags
                case 10: return BigDecimal.valueOf(6000); // Musical Instruments
                case 11: return BigDecimal.valueOf(10000);// Event Equipment
                case 12: return BigDecimal.valueOf(1200); // Hostel Items
                case 13: return BigDecimal.valueOf(500);  // Study Materials
            }
        }
        if (name.contains("calculator")) return BigDecimal.valueOf(1500);
        if (name.contains("cycle") || name.contains("bike")) return BigDecimal.valueOf(7500);
        if (name.contains("table") || name.contains("chair")) return BigDecimal.valueOf(3000);
        return BigDecimal.valueOf(2000);
    }

    private double getDepreciationRate(String condition) {
        if ("New".equalsIgnoreCase(condition)) return 0.15;
        if ("Like New".equalsIgnoreCase(condition)) return 0.30;
        if ("Good".equalsIgnoreCase(condition)) return 0.50;
        if ("Fair".equalsIgnoreCase(condition)) return 0.70;
        return 0.50;
    }
}
