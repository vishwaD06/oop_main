package com.sharesphere.controller;

import com.sharesphere.dto.request.RideShareCreateDto;
import com.sharesphere.dto.response.RideShareResponse;
import com.sharesphere.service.RideShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideShareController {

    private final RideShareService rideShareService;
    private final com.sharesphere.repository.UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<RideShareResponse>> getUpcomingRides(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userDetails != null ? getUserId(userDetails) : null;
        return ResponseEntity.ok(rideShareService.getUpcomingRides(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideShareResponse> getRideById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userDetails != null ? getUserId(userDetails) : null;
        return ResponseEntity.ok(rideShareService.getRideById(id, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RideShareResponse>> getMyRides(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(rideShareService.getMyRides(userId));
    }

    @PostMapping
    public ResponseEntity<RideShareResponse> createRide(
            @Valid @RequestBody RideShareCreateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(rideShareService.createRide(dto, userId));
    }

    @PostMapping("/{rideId}/join")
    public ResponseEntity<RideShareResponse> joinRide(
            @PathVariable Long rideId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(rideShareService.joinRide(rideId, userId));
    }

    @PostMapping("/{rideId}/cancel")
    public ResponseEntity<RideShareResponse> cancelBooking(
            @PathVariable Long rideId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(rideShareService.cancelBooking(rideId, userId));
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new com.sharesphere.exception.ResourceNotFoundException("User not found"))
                .getId();
    }
}
