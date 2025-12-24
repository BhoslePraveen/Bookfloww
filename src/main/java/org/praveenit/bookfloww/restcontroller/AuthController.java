package org.praveenit.bookfloww.restcontroller;

import org.praveenit.bookfloww.dto.RefreshRequest;
import org.praveenit.bookfloww.dto.TokenResponse;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.service.OAuthService;
import org.praveenit.bookfloww.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private OAuthService oauthService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody RefreshRequest request) {

        RefreshTokenEntity storedToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken());

        return ResponseEntity.ok(
                oauthService.refreshTokens(storedToken)
        );
    }
}

