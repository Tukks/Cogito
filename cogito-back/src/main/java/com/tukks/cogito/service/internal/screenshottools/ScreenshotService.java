package com.tukks.cogito.service.internal.screenshottools;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.tukks.cogito.dto.response.ActionCard;
import com.tukks.cogito.dto.response.ActionEvent;
import com.tukks.cogito.dto.response.ActionType;
import com.tukks.cogito.entity.LinkEntity;
import com.tukks.cogito.repository.LinkRepository;
import com.tukks.cogito.service.ImageService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ScreenshotService {

	private final Logger logger = LogManager.getLogger(getClass());

	private ApplicationEventPublisher applicationEventPublisher;

	private ImageService imageService;

	private LinkRepository linkRepository;

	/**
	 * Bean for Selenium WebDriver
	 * Is this a good idea to have a bean? what if we have multiple screenshot at the same time?
	 *
	 * @return
	 * @throws URISyntaxException
	 */
	public FirefoxDriver getFirefoxDriver() throws URISyntaxException {
		URL urlGeckoDriver = this.getClass().getClassLoader().getResource("firefox/geckodriver.exe");
		URL urlExtensionCookieBanner = this.getClass().getClassLoader().getResource("firefox/istilldontcareaboutcookies-1.1.1.xpi");
		URL urlByPassPaywallClean = this.getClass().getClassLoader().getResource("firefox/bypass_paywalls_clean-3.1.0.0.xpi");

		System.setProperty("webdriver.gecko.driver", urlGeckoDriver.getPath());

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
		firefoxOptions.setHeadless(true);

		FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
		firefoxDriver.installExtension(Path.of(urlExtensionCookieBanner.toURI()));
		firefoxDriver.installExtension(Path.of(urlByPassPaywallClean.toURI()));

		return firefoxDriver;
	}

	public Optional<UUID> getScreenshot(final String url, final String oidcSub) {
		try {
			FirefoxDriver firefoxDriver = getFirefoxDriver();

			// Navigate to the URL you want to take a screenshot of
			firefoxDriver.get(url);
			firefoxDriver.switchTo().defaultContent();

			byte[] imageBytes = firefoxDriver.getFullPageScreenshotAs(OutputType.BYTES);

			logger.info("Screenshot took, saving to database...");
			return imageService.saveImageToDatabase(UUID.randomUUID().toString(), imageBytes, oidcSub);
		} catch (URISyntaxException e) {
			logger.error("An error with Screenshot occured");
		}
		return Optional.empty();
	}

	public void takeScreenshot(String url, String oidcSub, UUID idLinkEntity) {
		CompletableFuture<Optional<UUID>> cf1 =
			CompletableFuture.supplyAsync(() -> getScreenshot(url, oidcSub));

		cf1.thenAccept(uuid -> uuid.ifPresent(value -> this.saveToDatabase(idLinkEntity, value.toString(), oidcSub)));

	}

	void saveToDatabase(UUID idLinkEntity, String uuidImage, String oidcSub) {
		LinkEntity linkEntity = linkRepository.findById(idLinkEntity);
		linkEntity.setScreenshot(uuidImage);
		linkEntity.setOidcSub(oidcSub);
		linkRepository.save(linkEntity);

		applicationEventPublisher.publishEvent(new ActionEvent(this,
			ActionCard.builder().actionType(ActionType.EDIT)
				.ids(List.of(idLinkEntity))
				.cards(List.of(linkEntity)).build(), oidcSub));
	}
}
