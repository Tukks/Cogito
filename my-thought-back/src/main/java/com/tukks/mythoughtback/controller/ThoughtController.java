package com.tukks.mythoughtback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tukks.mythoughtback.dto.request.ThingsEditRequest;
import com.tukks.mythoughtback.repository.ThingsRepository;
import com.tukks.mythoughtback.service.NoteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ThoughtController {

	private final ThingsRepository thingsRepository;
	private final NoteService noteService;

	@GetMapping("/thoughts")
	public List<Object> getAllThoughts() {
		return thingsRepository.getAll();
	}

	@PostMapping("/save")
	public void save(@RequestBody String note) {
		noteService.save(note);
	}

	@PatchMapping("/{id}")
	public void editThings(@PathVariable Long id, @RequestBody ThingsEditRequest thingsEditRequest) {
		noteService.editThings(id, thingsEditRequest);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		noteService.delete(id);
	}
}
