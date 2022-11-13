package com.tukks.cogito.controller;

import javax.imageio.stream.ImageOutputStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tukks.cogito.dto.response.ImageDTO;
import com.tukks.cogito.service.ImageService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ImageController {

	private final ImageService imageService;


	@PostMapping("/image")
	public ImageDTO uploadImage(@RequestParam("image") MultipartFile file) {
		return imageService.uploadImage(file);
	}

	@GetMapping("/image/{id}")
	public ImageOutputStream getImage(@PathVariable("id") String id) {
		return imageService.returnFileImage(id);
	}
}
