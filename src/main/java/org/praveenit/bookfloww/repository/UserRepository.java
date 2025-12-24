package org.praveenit.bookfloww.repository;

import java.util.Optional;

import org.praveenit.bookfloww.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long>{
	public Optional<User> findByEmail(String email);

}
