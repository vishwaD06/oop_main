package com.sharesphere.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RideBookingResponse {
    private Long id;
    private Long rideId;
    private UserResponse passenger;
    private int seatsBooked;
    private LocalDateTime bookedAt;
}
