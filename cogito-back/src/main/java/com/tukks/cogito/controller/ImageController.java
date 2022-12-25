package com.tukks.cogito.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tukks.cogito.service.ImageService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@PostMapping("/image")
	public UUID uploadImage(@RequestParam("image") MultipartFile file) {
		return imageService.uploadImage(file);
	}

	@GetMapping(value = "/image/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> getImage(@PathVariable("id") final String id) {
		var image = imageService.getImage(id);

		HttpHeaders headers = new HttpHeaders();
		if (image.mimeType() != null) {
			headers.setContentType(MediaType.parseMediaType(image.mimeType()));
		}

		CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.DAYS)
			.noTransform()
			.mustRevalidate();

		return ResponseEntity.ok().headers(headers).cacheControl(cacheControl).body(image.resource());
	}
}
