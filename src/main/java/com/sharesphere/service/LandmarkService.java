package com.sharesphere.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandmarkService {

    private static final List<String> VERIFIED_LANDMARKS = List.of(
            "Central Library Foyer (Ground Floor)",
            "Student Activity Center (SAC) Plaza",
            "Main Food Court / Canteen",
            "Hostel 1 / Ramanujan Hall Gate",
            "Hostel 2 / Aryabhata Hall Gate",
            "Hostel 3 / Kalam Hall Gate",
            "Hostel 4 / Sarabhai Hall Gate",
            "Hostel A - Girls Wing Entrance",
            "Hostel B - Boys Wing Entrance",
            "Hostel C - International Hostel Lobby",
            "Academic Complex Block A",
            "Academic Complex Block B",
            "Sports Complex & Badminton Court",
            "Campus Main Gate (Gate 1)"
    );

    public List<String> getVerifiedLandmarks() {
        return VERIFIED_LANDMARKS;
    }
}
