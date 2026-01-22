package org.praveenit.bookfloww.service.google;

import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.security.impl.TokenError;
import org.praveenit.bookfloww.security.impl.TokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public final class SecurityUtil {
	private SecurityUtil() {
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new TokenException(TokenError.USER_NOT_FOUND);
		}
		return (User) authentication.getPrincipal();
	}

}
