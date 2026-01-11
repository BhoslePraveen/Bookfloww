package org.praveenit.bookfloww.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/")
    public ResponseEntity<?> getDashboard() {
        // Example data, just for testing
        return ResponseEntity.ok(Map.of(
            "message", "This is a protected dashboard",
            "timestamp", Instant.now()
        ));
    }
}

