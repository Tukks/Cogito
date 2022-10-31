package com.tukks.mythoughtback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tukks.mythoughtback.dto.request.LinkRequest;
import com.tukks.mythoughtback.entity.LinkEntity;
import com.tukks.mythoughtback.repository.LinkRepository;
import com.tukks.mythoughtback.repository.ThoughRepository;
import com.tukks.mythoughtback.service.LinkService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ThoughtController {

	private final ThoughRepository thoughRepository;

	private final LinkRepository linkRepository;
	private final LinkService linkService;

	@GetMapping("/thoughts")
	public List<LinkEntity> getAllThoughts() {
		return linkRepository.getAll();
	}

	@PostMapping("/thoughts/link")
	public void saveLinkg(@RequestBody LinkRequest linkRequest) {
		linkService.addLinkWithPreview(linkRequest);
	}
}
