package com.tukks.cogito.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.tukks.cogito.entity.ImageEntity;
import com.tukks.cogito.repository.ImageRepository;

import static com.tukks.cogito.service.SecurityUtils.getSub;

@Service
public class ImageService {

	public record FileImage(InputStreamResource resource, String mimeType) {}

	@Value("${cogito.image.folder}")
	private String imageUploadFolder;

	private final ImageRepository imageRepository;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	public void uploadImage(MultipartFile file) {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
		String fileName = date + file.getOriginalFilename();

		String folderPath = imageUploadFolder;
		String filePath = folderPath + File.separator + fileName;
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			ImageEntity imageEntity = ImageEntity.builder().oidcSub(getSub()).imagePath(filePath).build();
			imageRepository.save(imageEntity);

		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "problem with file");
		}

	}

	public Optional<UUID> uploadImageFromUrl(final String imageUrl) {
		final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS-"));
		final String folderPath = imageUploadFolder;

		String filePath = folderPath + File.separator + date;

		try {
			// Open a connection to the URL
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			int status = connection.getResponseCode();

			// Follow redirect response if needed
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER) {

					String newUrl = connection.getHeaderField("Location");
					connection = (HttpURLConnection) new URL(newUrl).openConnection();
				}
			}
			// Determine the image format based on the URL
			String contentType = connection.getHeaderField("Content-Type");
			if (!contentType.startsWith("image/")) {
				return Optional.empty();
			}
			String[] parts = contentType.split("/");
			String imageFormat = parts[parts.length - 1];

			// Determine the file name based on the URL
			String[] urlParts = imageUrl.split("/");
			String fileName = urlParts[urlParts.length - 1];

			// Set up streams
			InputStream is = connection.getInputStream();
			filePath = filePath + fileName + "." + imageFormat;
			OutputStream os = new FileOutputStream(filePath);

			// Read from is and write to os
			byte[] buffer = new byte[4096];
			int len;
			while ((len = is.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}

			// Clean up
			os.close();
			is.close();
		} catch (IOException e) {
			// the image is unreachable or other problem, do something
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "problem with file");
		}
		ImageEntity imageEntity = ImageEntity.builder().oidcSub(getSub()).imagePath(filePath).build();
		var imageEntitySaved = imageRepository.save(imageEntity);
		return Optional.of(imageEntitySaved.getId());
	}

	public FileImage getImage(String id) {
		ImageEntity imageEntity = imageRepository.getByIdAndOidcSub(UUID.fromString(id), getSub());

		if (imageEntity == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with id");
		}
		String mimeType = URLConnection.guessContentTypeFromName(imageEntity.getImagePath());

		InputStreamResource imageOutputStream;
		try {
			imageOutputStream = new InputStreamResource(new FileInputStream(imageEntity.getImagePath()));
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "problem with file");

		}

		return new FileImage(imageOutputStream, mimeType);
	}
}
