package com.tukks.cogito.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.tukks.cogito.dto.response.ImageDTO;
import com.tukks.cogito.entity.image.ImageEntity;
import com.tukks.cogito.repository.ImageRepository;

import static com.tukks.cogito.service.SecurityUtils.getSub;

@Service
public class ImageService {

	@Value("${cogito.image.folder}")
	private String imageUploadFolder;

	private final ImageRepository imageRepository;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	public ImageDTO uploadImage(MultipartFile file) {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
		String fileName = date + file.getOriginalFilename();

		String folderPath = imageUploadFolder;
		String filePath = folderPath + File.separator + fileName;
		ImageEntity imageEntitySaved;
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			ImageEntity imageEntity = ImageEntity.builder().oidcSub(getSub()).imagePath(filePath).build();
			imageEntitySaved = imageRepository.save(imageEntity);

		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "problem with file");
		}
		return new ImageDTO(imageEntitySaved.getId());
	}

	public ImageOutputStream returnFileImage(String id) {
		ImageEntity imageEntity = imageRepository.getByIdAndOidcSub(Long.valueOf(id), getSub());

		if (imageEntity == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with id");
		}

		ImageOutputStream imageOutputStream;
		try {
			imageOutputStream = new FileImageOutputStream(new File(imageEntity.getImagePath()));
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "problem with file");

		}

		return imageOutputStream;
	}
}
