package org.praveenit.bookfloww.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.praveenit.bookfloww.service.GoogleAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/oauth/google")
@RequiredArgsConstructor
public class GoogleLoginController {
    private final GoogleAuthService googleAuthService;

    @GetMapping("/login")
    public void loginWithGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleAuthService.buildGoogleLoginUrl());
    }
}
