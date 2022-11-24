package com.tukks.cogito.service.internal.languagedetector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

import static com.github.pemistahl.lingua.api.Language.ENGLISH;
import static com.github.pemistahl.lingua.api.Language.FRENCH;

@Service
public class LanguageDetector {

	private final Logger logger = LogManager.getLogger(getClass());

	public Language detectLanguage(String text) {
		logger.info("detecting language...");
		final com.github.pemistahl.lingua.api.LanguageDetector detector = LanguageDetectorBuilder.fromLanguages(ENGLISH, FRENCH).build();
		return detector.detectLanguageOf(text);
	}
}
