package com.tukks.mythoughtback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tukks.mythoughtback.dto.request.LinkRequest;
import com.tukks.mythoughtback.repository.LinkRepository;
import com.tukks.mythoughtback.repository.ThingsRepository;
import com.tukks.mythoughtback.service.LinkService;
import com.tukks.mythoughtback.service.SaveNote;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ThoughtController {

	private final LinkRepository linkRepository;
	private final LinkService linkService;

	private final ThingsRepository thingsRepository;
	private final SaveNote saveNote;

	@GetMapping("/thoughts")
	public List<Object> getAllThoughts() {
		return thingsRepository.getAll();
	}

	@PostMapping("/thoughts/link")
	public void saveLinkg(@RequestBody LinkRequest linkRequest) {
		linkService.addLinkWithPreview(linkRequest);
	}

	@PostMapping("/save")
	public void save(@RequestBody String note) {
		saveNote.save(note);
	}
}
