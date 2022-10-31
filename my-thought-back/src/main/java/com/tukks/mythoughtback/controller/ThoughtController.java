package com.tukks.mythoughtback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tukks.mythoughtback.entity.ThoughtEntity;
import com.tukks.mythoughtback.repository.ThoughRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ThoughtController {

	private final ThoughRepository thoughRepository;

	@GetMapping("/thoughts")
	public List<ThoughtEntity> getAllThoughts() {
		return thoughRepository.getAll();
	}
}
