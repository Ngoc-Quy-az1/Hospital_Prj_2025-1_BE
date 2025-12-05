package com.example.Hospital.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller cho root path và các endpoints công khai
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "message", "Hospital Management System API",
            "version", "1.0.0",
            "status", "running",
            "endpoints", Map.of(
                "auth", "/api/auth/**",
                "admin", "/api/admin/**",
                "doctor", "/api/doctor/**",
                "patient", "/api/patient/**",
                "health", "/actuator/health"
            )
        );
    }
}

