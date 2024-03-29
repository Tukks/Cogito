package com.tukks.cogito.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tukks.cogito.entity.UserEntity;
import com.tukks.cogito.service.LoginService;
import com.tukks.cogito.utils.JwtTokenUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginController {

	private final Logger logger = LogManager.getLogger(getClass());

	private AuthenticationManager authManager;
	private JwtTokenUtil jwtUtil;

	private LoginService loginService;


	private record AuthRequest(@Email String email, String password) {}

	private record AuthRegister(@Email String email, String password) {}

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AuthRequest request, HttpServletResponse response) {
		try {
			logger.info("Start Authentification");
			Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					request.email, request.password)
			);

			UserEntity user = (UserEntity)authentication.getPrincipal();
			jwtUtil.createCookieWithToken(response, user, jwtUtil);

			return ResponseEntity.ok().build();

		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/register")
	public void register(@RequestBody @Valid AuthRegister request) {
		logger.info("Start Register");

		loginService.register(request.email, request.password);

	}

	@GetMapping("/logout")
	public ResponseEntity logout(HttpServletResponse response) {
		logger.info("Start Register");

		jwtUtil.removeCookieOnLogout(response);

		return ResponseEntity.ok().build();
	}

}
