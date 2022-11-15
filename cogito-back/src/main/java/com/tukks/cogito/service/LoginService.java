package com.tukks.cogito.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tukks.cogito.entity.UserEntity;
import com.tukks.cogito.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {

	private PasswordEncoder passwordEncoder;

	private UserRepository userRepository;

	public void register(String email, String password) {
		Optional<UserEntity> userEntity = userRepository.findByUsername(email);
		if (userEntity.isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Account already exist");
		}
		UserEntity user = new UserEntity();
		user.setUsername(email);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}
}
