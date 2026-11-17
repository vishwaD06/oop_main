package com.sharesphere.service.impl;

import com.sharesphere.dto.request.RideShareCreateDto;
import com.sharesphere.dto.response.RideBookingResponse;
import com.sharesphere.dto.response.RideShareResponse;
import com.sharesphere.dto.response.UserResponse;
import com.sharesphere.entity.*;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.exception.ValidationException;
import com.sharesphere.repository.RideBookingRepository;
import com.sharesphere.repository.RideShareRepository;
import com.sharesphere.repository.UserRepository;
import com.sharesphere.service.NotificationService;
import com.sharesphere.service.RideShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RideShareServiceImpl implements RideShareService {

    private final RideShareRepository rideShareRepository;
    private final RideBookingRepository rideBookingRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public List<RideShareResponse> getUpcomingRides(Long currentUserId) {
        return rideShareRepository.findByStatusInOrderByDepartureTimeAsc(List.of(RideStatus.OPEN, RideStatus.FULL))
                .stream().map(r -> toResponse(r, currentUserId)).toList();
    }

    @Override
    public RideShareResponse getRideById(Long id, Long currentUserId) {
        RideShare ride = rideShareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        return toResponse(ride, currentUserId);
    }

    @Override
    @Transactional
    public RideShareResponse createRide(RideShareCreateDto dto, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RideShare ride = RideShare.builder()
                .organizer(organizer)
                .origin(dto.getOrigin())
                .destination(dto.getDestination())
                .departureTime(dto.getDepartureTime())
                .totalSeats(dto.getTotalSeats())
                .availableSeats(dto.getTotalSeats())
                .pricePerSeat(dto.getPricePerSeat())
                .vehicleType(dto.getVehicleType())
                .notes(dto.getNotes())
                .status(RideStatus.OPEN)
                .build();

        return toResponse(rideShareRepository.save(ride), organizerId);
    }

    @Override
    @Transactional
    public RideShareResponse joinRide(Long rideId, Long passengerId) {
        RideShare ride = rideShareRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        if (ride.getOrganizer().getId().equals(passengerId)) {
            throw new ValidationException("Cannot book your own ride");
        }
        if (ride.getAvailableSeats() <= 0) {
            throw new ValidationException("No available seats left on this ride");
        }
        if (rideBookingRepository.existsByRideIdAndPassengerId(rideId, passengerId)) {
            throw new ValidationException("You have already booked a seat on this ride");
        }

        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RideBooking booking = RideBooking.builder()
                .ride(ride)
                .passenger(passenger)
                .seatsBooked(1)
                .build();
        rideBookingRepository.save(booking);

        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        if (ride.getAvailableSeats() == 0) {
            ride.setStatus(RideStatus.FULL);
        }
        ride = rideShareRepository.save(ride);

        notificationService.sendNotification(
                ride.getOrganizer().getId(),
                "New Passenger Joined Your Ride",
                passenger.getName() + " joined your ride to " + ride.getDestination(),
                NotificationType.RENTAL_ACCEPTED,
                ride.getId(),
                "RIDE"
        );

        return toResponse(ride, passengerId);
    }

    @Override
    @Transactional
    public RideShareResponse cancelBooking(Long rideId, Long passengerId) {
        RideShare ride = rideShareRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        List<RideBooking> bookings = rideBookingRepository.findByRideId(rideId);
        RideBooking userBooking = bookings.stream()
                .filter(b -> b.getPassenger().getId().equals(passengerId))
                .findFirst()
                .orElseThrow(() -> new ValidationException("You do not have a booking on this ride"));

        rideBookingRepository.delete(userBooking);
        ride.setAvailableSeats(ride.getAvailableSeats() + 1);
        if (ride.getStatus() == RideStatus.FULL) {
            ride.setStatus(RideStatus.OPEN);
        }
        ride = rideShareRepository.save(ride);

        return toResponse(ride, passengerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideShareResponse> getMyRides(Long userId) {
        Map<Long, RideShare> rides = new LinkedHashMap<>();
        rideShareRepository.findByOrganizerIdOrderByCreatedAtDesc(userId)
                .forEach(r -> rides.put(r.getId(), r));
        rideBookingRepository.findByPassengerIdOrderByBookedAtDesc(userId)
                .forEach(b -> rides.putIfAbsent(b.getRide().getId(), b.getRide()));
        return rides.values().stream()
                .sorted(Comparator.comparing(RideShare::getDepartureTime).reversed())
                .map(r -> toResponse(r, userId)).toList();
    }

    private RideShareResponse toResponse(RideShare ride, Long currentUserId) {
        List<RideBooking> bookings = rideBookingRepository.findByRideId(ride.getId());
        boolean userJoined = currentUserId != null && bookings.stream()
                .anyMatch(b -> b.getPassenger().getId().equals(currentUserId));

        List<RideBookingResponse> bookingResponses = bookings.stream()
                .map(b -> RideBookingResponse.builder()
                        .id(b.getId())
                        .rideId(ride.getId())
                        .passenger(toUserResponse(b.getPassenger()))
                        .seatsBooked(b.getSeatsBooked())
                        .bookedAt(b.getBookedAt())
                        .build())
                .toList();

        return RideShareResponse.builder()
                .id(ride.getId())
                .organizer(toUserResponse(ride.getOrganizer()))
                .origin(ride.getOrigin())
                .destination(ride.getDestination())
                .departureTime(ride.getDepartureTime())
                .totalSeats(ride.getTotalSeats())
                .availableSeats(ride.getAvailableSeats())
                .pricePerSeat(ride.getPricePerSeat())
                .vehicleType(ride.getVehicleType())
                .notes(ride.getNotes())
                .status(ride.getStatus())
                .bookings(bookingResponses)
                .createdAt(ride.getCreatedAt())
                .userJoined(userJoined)
                .build();
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .college(user.getCollege())
                .location(user.getLocation())
                .averageRating(user.getAverageRating())
                .totalRatings(user.getTotalRatings())
                .build();
    }
}
