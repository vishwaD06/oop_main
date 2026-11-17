package com.sharesphere.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ModerationCheckResponse {
    private boolean approved;
    private boolean flagged;
    private String categoryDetected;
    private List<String> flaggedKeywords;
    private String violationPolicy;
    private String message;
}
