package com.tukks.cogito.service;

import org.springframework.security.core.context.SecurityContextHolder;

import com.tukks.cogito.entity.UserEntity;

public class SecurityUtils {

	public static String getSub() {
		return ((UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().toString();
	}
}
