package org.praveenit.bookfloww.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.praveenit.bookfloww.dto.RefreshRequest;
import org.praveenit.bookfloww.dto.auth.AuthTokenData;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.service.OAuthService;
import org.praveenit.bookfloww.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RefreshTokenService refreshTokenService;
    private final OAuthService oauthService;

    @PostMapping("/refresh")
    public ResponseEntity<AuthTokenData> refreshToken(@RequestBody @Valid RefreshRequest request) {
        RefreshTokenEntity storedToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(oauthService.refreshTokens(storedToken));
    }
}

