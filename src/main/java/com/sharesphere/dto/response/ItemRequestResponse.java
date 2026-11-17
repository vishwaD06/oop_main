package com.sharesphere.dto.response;

import com.sharesphere.entity.RequestStatus;
import com.sharesphere.entity.RequestUrgency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestResponse {
    private Long id;
    private String title;
    private String description;
    private CategoryResponse category;
    private UserResponse requester;
    private String location;
    private String landmark;
    private BigDecimal budget;
    private String neededTimeframe;
    private RequestUrgency urgency;
    private RequestStatus status;
    private int offerCount;
    private List<OfferResponseDto> offers;
    private LocalDateTime createdAt;
}
