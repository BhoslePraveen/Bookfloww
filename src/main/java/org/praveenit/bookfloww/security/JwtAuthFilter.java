package org.praveenit.bookfloww.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.repository.UserRepository;
import org.praveenit.bookfloww.security.impl.TokenError;
import org.praveenit.bookfloww.security.impl.TokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;

	private final UserRepository userRepository;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.equals("/") || path.startsWith("/auth/") || path.startsWith("/oauth/") || path.startsWith("/error");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			SecurityContextHolder.clearContext();
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(7);

		try {
			Claims claims = jwtUtil.validateAndGetClaims(token);
			String email = claims.getSubject(); // subject=email
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new TokenException(TokenError.USER_NOT_FOUND));
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, // USER
																												// as
																												// principal
					null, authorities);

			authentication.setDetails(request);// 1st
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (ExpiredJwtException e) {
			SecurityContextHolder.clearContext();// 2nd
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("""
					{
					  "error": "access_token_expired",
					  "error_description": "Access token has expired",
					  "action": "REFRESH"
					}
					""");
			return;
		} catch (JwtException e) {
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("""
					{
					  "error": "invalid_grant",
					  "error_description": "Invalid access token",
					  "action": "RE_LOGIN"
					}
					""");
			return;
		}

		filterChain.doFilter(request, response);
	}
}
