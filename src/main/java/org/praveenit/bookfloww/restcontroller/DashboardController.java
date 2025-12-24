package org.praveenit.bookfloww.restcontroller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        // Example data, just for testing
        return ResponseEntity.ok(Map.of(
            "message", "This is a protected dashboard",
            "timestamp", Instant.now()
        ));
    }
}

