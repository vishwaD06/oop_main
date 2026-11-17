package com.sharesphere.controller;

import com.sharesphere.service.LandmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/landmarks")
@RequiredArgsConstructor
public class LandmarkController {

    private final LandmarkService landmarkService;

    @GetMapping
    public ResponseEntity<List<String>> getVerifiedLandmarks() {
        return ResponseEntity.ok(landmarkService.getVerifiedLandmarks());
    }
}
