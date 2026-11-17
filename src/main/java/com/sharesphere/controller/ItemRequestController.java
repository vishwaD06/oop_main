package com.sharesphere.controller;

import com.sharesphere.dto.request.ItemRequestCreateDto;
import com.sharesphere.dto.request.OfferCreateDto;
import com.sharesphere.dto.response.ItemRequestResponse;
import com.sharesphere.dto.response.OfferResponseDto;
import com.sharesphere.service.ItemRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final com.sharesphere.repository.UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ItemRequestResponse>> getAllOpenRequests() {
        return ResponseEntity.ok(itemRequestService.getAllOpenRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestResponse> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(itemRequestService.getRequestById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ItemRequestResponse>> getMyRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(itemRequestService.getMyRequests(userId));
    }

    @PostMapping
    public ResponseEntity<ItemRequestResponse> createRequest(
            @Valid @RequestBody ItemRequestCreateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(itemRequestService.createRequest(dto, userId));
    }

    @PostMapping("/{requestId}/offers")
    public ResponseEntity<OfferResponseDto> addOffer(
            @PathVariable Long requestId,
            @Valid @RequestBody OfferCreateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(itemRequestService.addOffer(requestId, dto, userId));
    }

    @PostMapping("/{requestId}/fulfill")
    public ResponseEntity<ItemRequestResponse> fulfillRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(itemRequestService.fulfillRequest(requestId, userId));
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new com.sharesphere.exception.ResourceNotFoundException("User not found"))
                .getId();
    }
}
