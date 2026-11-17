package com.sharesphere.repository;

import com.sharesphere.entity.RideBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideBookingRepository extends JpaRepository<RideBooking, Long> {
    List<RideBooking> findByRideId(Long rideId);
    List<RideBooking> findByPassengerIdOrderByBookedAtDesc(Long passengerId);
    boolean existsByRideIdAndPassengerId(Long rideId, Long passengerId);
}
