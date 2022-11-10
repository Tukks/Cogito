package com.tukks.cogito.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@PropertySource("application.properties")

public class SecurityConfig {

	@Value("${auth.login-success-redirect-uri}")
	private String redirectUri;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.oauth2Login().defaultSuccessUrl(redirectUri);
		return http.build();
	}
}