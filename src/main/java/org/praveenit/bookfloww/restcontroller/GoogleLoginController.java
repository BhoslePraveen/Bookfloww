package org.praveenit.bookfloww.restcontroller;

import java.io.IOException;

import org.praveenit.bookfloww.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/oauth/google")
public class GoogleLoginController {

	private final GoogleAuthService googleAuthService;

	public GoogleLoginController(GoogleAuthService googleAuthService) {
		this.googleAuthService = googleAuthService;
	}

	@GetMapping("/login")
	public void loginWithGoogle(HttpServletResponse response) throws IOException {
		response.sendRedirect(googleAuthService.buildGoogleLoginUrl());
	}
}
