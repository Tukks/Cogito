package com.tukks.cogito.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

	public static String getSub() {
		return (String)((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaims().get("sub");
	}
}
