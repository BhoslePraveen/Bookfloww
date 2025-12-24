package org.praveenit.bookfloww.security;

import java.io.IOException;

import org.praveenit.bookfloww.security.impl.TokenError;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        TokenError error =
                (TokenError) request.getAttribute("TOKEN_ERROR");

        if (error == null) {
            error = TokenError.INVALID_GRANT;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write("""
        {
          "error": "%s",
          "error_description": "%s",
          "action": "%s"
        }
        """.formatted(
                error.getCode(),
                error.getDescription(),
                error.getAction()
        ));
    }
}
