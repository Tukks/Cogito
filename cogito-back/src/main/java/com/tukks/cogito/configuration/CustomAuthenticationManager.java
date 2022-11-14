package com.tukks.cogito.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tukks.cogito.entity.UserEntity;
import com.tukks.cogito.repository.UserRepository;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

	private UserRepository userRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		UserEntity user = userRepo.findByUsername((String)authentication.getPrincipal())
			.orElseThrow(() -> new UsernameNotFoundException("User " + authentication.getPrincipal() + " not found."));

		return new UsernamePasswordAuthenticationToken(
			user, authentication.getCredentials(), new ArrayList<>());
	}
}
