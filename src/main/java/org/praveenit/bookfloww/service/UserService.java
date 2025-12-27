package org.praveenit.bookfloww.service;

import java.time.LocalDate;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;

	public User findOrCreateGoogleUser(String email, String name) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			User user = new User();
			user.setEmail(email);
			user.setName(name);
			user.setRole(User.UserRole.CLIENT);
			user.setIsActive(true);
			// Save first (ID generated here)
			user = userRepository.save(user);

			// Generate customer code using DB ID
			String customerCode = generateCustomerCode(user.getId());
			user.setCustomerCode(customerCode);

			// Update user
			return userRepository.save(user);
		});
	}

	private String generateCustomerCode(Long id) {

		LocalDate now = LocalDate.now();
		String month = String.format("%02d", now.getMonthValue());
		String year = String.valueOf(now.getYear()).substring(2);

		// Format ID to 4 digits (expand to 5-6 if needed)
		return "CUST_" + month + year + String.format("%04d", id);
	}

}
