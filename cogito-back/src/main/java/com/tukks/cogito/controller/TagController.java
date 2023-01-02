package com.tukks.cogito.controller;

import java.util.List;

import javax.ws.rs.PathParam;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tukks.cogito.repository.TagRepository;

import lombok.AllArgsConstructor;
import static com.tukks.cogito.service.SecurityUtils.getSub;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TagController {

	private final TagRepository tagRepository;

	@GetMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getTags(@PathParam("startWith") final String startWith) {
		List<String> tags = this.tagRepository.getTagsByOidcSubAndHiddenAndTagStartsWithIgnoreCase(getSub(),
				false, startWith)
			.stream()
			.map(tag -> tag.getTag().toLowerCase())
			.distinct()
			.toList();

		return ResponseEntity.ok(tags);
	}
}
