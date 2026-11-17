package com.sharesphere.dto.response;

import com.sharesphere.entity.RideStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RideShareResponse {
    private Long id;
    private UserResponse organizer;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int totalSeats;
    private int availableSeats;
    private BigDecimal pricePerSeat;
    private String vehicleType;
    private String notes;
    private RideStatus status;
    private List<RideBookingResponse> bookings;
    private LocalDateTime createdAt;
    private boolean userJoined;
}
