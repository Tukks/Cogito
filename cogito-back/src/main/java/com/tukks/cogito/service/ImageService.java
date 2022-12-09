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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private final Logger logger = LogManager.getLogger(getClass());

	public static final String INVALID_FILENAME_CHAR_REGEX = "[\\\\/:*?\"<>|.&]";

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

		// TODO Handle base 64 link like this one :
		// https://www.reddit.comdata:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAE9 0lEQVRoQ9WYW0hlZRTHf94TIXXAYRQzQwmFQQSVYSZ6UlESH+whRxMDX6IHJ1AQCXFQkSFUCIUeCkETUx/CF1FB8ClNpFFEX8T UKWkyxgFngjRvJ9Z278Pe57Yv51Kul33O3mut7/9fa32X9UVxwyXqhuMn0gR+BbJ0QfsU+CaYIEaSgEuAulzKQ3lGR0fLzzngA 6ckIkpAA68HGxWlQHCMw7Ghg4i5AhBIBE4d+HTO3OZgscB5bW0tk5OTbtPV1VXu3bv3v8/AZ8DXwAmQODw8TFNTE0dHR6SlpWl kHFeCY0MLGSgE1lW9D4Fp4AdAfmvyO5BpwZdflVATuF5ijJIAnAUDMpBtuAhIYa+GC7RhFQvxIFoGQh2YiJXQR8AU8C6wE+Lg+ HQXjkhFNAvhICCREhLh8m3IRLgGEQIXgGxgmqwA3wHfqt9CUmGhIvAe8KMDRPGyQzuwc5sES+AXIEe8tbS0MDAwYAnL2dkZt2/ f5tWrV5p+nNOsmBH4EmjzgeoKiE5ISOD01NEZzO3y6uqKmJgY7X8S8DdwB/hDffkx8L2/yPgj0AwMipGAfPjwIS9fvmRmZsbtx 9fJ0lL4/Sitra1RVFQUyMUn6hwyncTPgLcrKyuZm5Ne41ok3SkpKVRVVRmIBAPal63aH7gbH9GJj4/n/FyZKk+AL/R2nhmQqDf v7++TnZ3t1uvu7ubx48dsbGxQUFAQasxe/pKSkpQ5Ijg0SUxM1MrVgFn/R35fNTQ0MDY25jZ8/fo1ycnJHB8fK89Iya1bt9ja2 iIjI8M9pJYd/R6jJ2DoWTUrMRocHKS5WaZFZEWifnIibcS1TE1NKfPRLwH5ODEx4TY4PDwkKysLWfb+K6mpqWF6WlqJa/HsoQ0 ZCPXKEg7SISOQk5PD3t6eAePFxYV+TTfFr6tpRXd0dJTGxsaAdgEJLC0t8eDBA8WBbDDqvY2Xw9jYWC4vL5X3vb29jIyMsLNzf Xq2mkUNfGdnJ11dXVppBLRva2ujr6+vF+hwl5QOnUsAa8BkAF9gdnd3yc3N5e7du2xubhrIic3CwgJlZWWm0ZcgSMb0Ivb3799 neXnZy97XCmSYzerMvtJA+yPga6MxRWtDQUgJOU9Rx5U7mToDaQ9FWbPekPLRbRwGlbq6OsNKZQNbUKr+bvB8nYX+kd1bDlieK Q4KQZDGdggoc9HOhAyETXZTqWk5tebn51NeXu6Iil0CcpS9Y3VF0SNKTU1Vjh1WRGpdPaQFVC8uLubp06ey7HlNDn/HaeV2wS4 Bz3XdCgkrY6h+ZX3/yWtyBxjEVVJSglzAWpUwE/AZ7EAdme15sL29TV5enlW+dHR00NPTE1C/tLSUxcVF2fKV1tVOBt4B9trb2 3nyRPoIa2InCzbKx2+gzXpi21kQmlZIyF5jpqd+l/74T3/hMyPgaEl98eKF0lH5k/X1dQoL5fbdv1RXV0vr+jNQEkjPCgFHJMR IQEobqklmZiYHBwemtSh99+zsrJJMM2VTBZ0DpZwqKiqYn5838+v4e1xcnHYCsITNkpIOzbZ688zz589JT093DNTTcGVlRTmJq jd871t1bJeA5lfOwcptVH9/P62trVbH89IbHx9HLhJUsY3HtoEHgt+At7R39fX1CCAzGRoa4tGjR3q1N4G/zOx8fQ+WgN7n58B XNkAIgyEb+j5VQ0kgWCyO7G88gX8BA0iEQE02M2MAAAAASUVORK5CYII=
		if (imageUrl.contains("base64")) {
			return Optional.empty();
		}

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
			filePath = filePath + fileName.replaceAll(INVALID_FILENAME_CHAR_REGEX, "") + "." + imageFormat;
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
			logger.error("unsupported file", e);
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
