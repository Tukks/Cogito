package com.tukks.cogito.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tukks.cogito.entity.UserEntity;
import com.tukks.cogito.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {

	private PasswordEncoder passwordEncoder;

	private UserRepository userRepository;

	public void register(String email, String password) {
		UserEntity user = new UserEntity();
		user.setUsername(email);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}
}
