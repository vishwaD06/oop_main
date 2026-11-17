package com.sharesphere.service;

import com.sharesphere.dto.request.RideShareCreateDto;
import com.sharesphere.dto.response.RideShareResponse;

import java.util.List;

public interface RideShareService {
    List<RideShareResponse> getUpcomingRides(Long currentUserId);
    RideShareResponse getRideById(Long id, Long currentUserId);
    RideShareResponse createRide(RideShareCreateDto dto, Long organizerId);
    RideShareResponse joinRide(Long rideId, Long passengerId);
    RideShareResponse cancelBooking(Long rideId, Long passengerId);
    List<RideShareResponse> getMyRides(Long userId);
}
