package com.sharesphere.controller;

import com.sharesphere.dto.request.PricingRecommendationRequest;
import com.sharesphere.dto.request.ModerationCheckRequest;
import com.sharesphere.dto.response.ModerationCheckResponse;
import com.sharesphere.dto.response.PricingRecommendationResponse;
import com.sharesphere.service.DynamicPricingService;
import com.sharesphere.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/smart")
@RequiredArgsConstructor
public class SmartFeaturesController {

    private final DynamicPricingService dynamicPricingService;
    private final ModerationService moderationService;

    @PostMapping("/pricing")
    public ResponseEntity<PricingRecommendationResponse> getSmartPricing(
            @RequestBody PricingRecommendationRequest req) {
        return ResponseEntity.ok(dynamicPricingService.recommendPricing(req));
    }

    @PostMapping("/moderation/check")
    public ResponseEntity<ModerationCheckResponse> checkModeration(
            @RequestBody ModerationCheckRequest req) {
        return ResponseEntity.ok(moderationService.checkText(req));
    }
}
