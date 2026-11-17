package com.sharesphere.service;

import com.sharesphere.dto.request.ModerationCheckRequest;
import com.sharesphere.dto.response.ModerationCheckResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ModerationService {

    private record Rule(String category, String policy, List<String> triggers) {}

    private static final List<Rule> RULES = List.of(
            new Rule(
                    "HAZARDOUS_HOSTEL_APPLIANCE",
                    "Campus Hostel Safety Regulation 4.2 — High-wattage heating coils and immersion rods are strictly prohibited in hostel rooms due to fire hazard.",
                    List.of("immersion rod", "coil heater", "room heater", "electric stove", "hot plate", "heating coil")
            ),
            new Rule(
                    "ACADEMIC_INTEGRITY_VIOLATION",
                    "Academic Integrity Code Article 9 — Unauthorized distribution of exam materials, proxy attendance, or forged academic credentials is prohibited.",
                    List.of("leaked paper", "exam leak", "proxy attendance", "fake id", "fake hall ticket", "exam cheat", "cheat sheet", "proxy exam")
            ),
            new Rule(
                    "WEAPONS_AND_HAZARDS",
                    "Campus Safety & Security Policy 1.1 — Weapons, blades, firearms, or hazardous implements are strictly barred from the marketplace.",
                    List.of("knife", "dagger", "sword", "gun", "air gun", "brass knuckle", "firearm", "katana", "explosive")
            ),
            new Rule(
                    "SUBSTANCE_POLICY_VIOLATION",
                    "Campus Substance-Free Regulations 3.0 — Sale or exchange of alcohol, vapes, tobacco, or illicit substances is strictly prohibited on campus.",
                    List.of("alcohol", "beer", "whiskey", "vodka", "rum", "weed", "marijuana", "ganja", "bong", "vape", "e-cigarette", "cigarette", "tobacco")
            )
    );

    public ModerationCheckResponse checkText(ModerationCheckRequest req) {
        String fullText = String.format("%s %s %s",
                req.getName() != null ? req.getName() : "",
                req.getDescription() != null ? req.getDescription() : "",
                req.getLocation() != null ? req.getLocation() : ""
        ).toLowerCase();

        for (Rule rule : RULES) {
            List<String> matched = new ArrayList<>();
            for (String trigger : rule.triggers()) {
                if (fullText.contains(trigger)) {
                    matched.add(trigger);
                }
            }
            if (!matched.isEmpty()) {
                return ModerationCheckResponse.builder()
                        .approved(false)
                        .flagged(true)
                        .categoryDetected(rule.category())
                        .flaggedKeywords(matched)
                        .violationPolicy(rule.policy())
                        .message("Listing rejected by Automated Campus Safety Moderation: Prohibited item '" + matched.get(0) + "'. " + rule.policy())
                        .build();
            }
        }

        return ModerationCheckResponse.builder()
                .approved(true)
                .flagged(false)
                .categoryDetected("NONE")
                .flaggedKeywords(Collections.emptyList())
                .violationPolicy(null)
                .message("Listing passes all campus safety and academic integrity checks.")
                .build();
    }
}
