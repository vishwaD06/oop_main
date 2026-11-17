package com.sharesphere.controller;

import com.sharesphere.dto.request.TopUpRequest;
import com.sharesphere.dto.response.WalletResponse;
import com.sharesphere.security.JwtAuthFilter;
import com.sharesphere.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final com.sharesphere.repository.UserRepository userRepository;

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(walletService.getWallet(userId));
    }

    @PostMapping("/topup")
    public ResponseEntity<WalletResponse> topUp(@AuthenticationPrincipal UserDetails userDetails,
                                                 @Valid @RequestBody TopUpRequest req) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(walletService.topUp(userId, req.getAmount()));
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new com.sharesphere.exception.ResourceNotFoundException("User not found"))
                .getId();
    }
}
