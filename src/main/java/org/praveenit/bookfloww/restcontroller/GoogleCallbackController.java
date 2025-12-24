package org.praveenit.bookfloww.restcontroller;

import java.time.Instant;
import java.util.Map;

import org.praveenit.bookfloww.dto.TokenData;
import org.praveenit.bookfloww.dto.TokenResponse;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.security.JwtUtil;
import org.praveenit.bookfloww.service.GoogleAuthService;
import org.praveenit.bookfloww.service.RefreshTokenService;
import org.praveenit.bookfloww.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/google")
public class GoogleCallbackController {
	@Autowired
	private GoogleAuthService googleAuthService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private UserService userService;
	
	@Value("${jwt.access.expiration}")
	private long accessTokenDurationMs;

	@GetMapping("/callback")
	public ResponseEntity<?> callback(@RequestParam("code") String code) {
		// exchange code for tokens
		Map<String, Object> tokens = googleAuthService.exchangeCodeForTokens(code);
		String googleAccessToken = (String) tokens.get("access_token");
		String googleRefreshToken = (String) tokens.get("refresh_token");

		// Fetch user profile using access token
		Map<String, Object> profile = googleAuthService.getUserProfile(googleAccessToken);
		String email = (String) profile.get("email");
		String name = (String) profile.get("name");


		User user = userService.findOrCreateGoogleUser(email, name);
		String jwtAccessToken = jwtUtil.generateAccessToken(user);
		// Create APP refresh token + store google refresh token
		String appRefreshToken = refreshTokenService.createRefreshToken(user, googleRefreshToken);

		TokenData tokenData = new TokenData(jwtAccessToken, appRefreshToken, accessTokenDurationMs / 1000,
				Instant.now().plusMillis(accessTokenDurationMs), 0, null);

		return ResponseEntity.ok(new TokenResponse(tokenData));

	}
}
