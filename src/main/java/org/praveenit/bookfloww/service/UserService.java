package org.praveenit.bookfloww.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User findOrCreateGoogleUser(String email, String name) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			User user = new User();
			user.setEmail(email);
			user.setName(name);
			// Update user
			return userRepository.save(user);
		});
	}
}
