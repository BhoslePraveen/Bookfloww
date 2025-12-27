package org.praveenit.bookfloww.restcontroller;

import org.praveenit.bookfloww.dto.TokenResponse;
import org.praveenit.bookfloww.service.GoogleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth/google")
@RequiredArgsConstructor
public class GoogleCallbackController {

    private final GoogleService googleService;

    @GetMapping("/callback")
    public ResponseEntity<TokenResponse> callback(
            @RequestParam("code") String code) {

        return ResponseEntity.ok(
                googleService.handleGoogleCallback(code)
        );
    }
}
