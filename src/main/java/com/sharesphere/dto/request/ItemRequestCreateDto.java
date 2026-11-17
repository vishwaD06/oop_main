package com.sharesphere.dto.request;

import com.sharesphere.entity.RequestUrgency;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemRequestCreateDto {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private Long categoryId;
    private String location;
    private String landmark;
    private BigDecimal budget;
    private String neededTimeframe;
    private RequestUrgency urgency = RequestUrgency.MEDIUM;
}
