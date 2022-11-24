package com.tukks.cogito.service.internal.NLP;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import edu.stanford.nlp.util.StringUtils;

@Service
public class TextCleaner {

	private final Logger logger = LogManager.getLogger(getClass());

	String matchPattern = "([^a-zA-Z]*)([a-zA-Z-\\.]*[a-zA-Z])([^a-zA-Z]*[a-zA-Z]*)";

	/**
	 * Normalizing the new line spaces in input text
	 *
	 * @param text
	 * @return
	 */
	public String normalizeText(String text) {

		return removeAccents(text).trim();
	}

	private static String normalize(String input) {
		return input == null ? null : Normalizer.normalize(input, Normalizer.Form.NFKD);
	}

	static String removeAccents(String input) {
		return normalize(input).replaceAll("\\p{M}", "");
	}

	/**
	 * Tokenizing the text using regex
	 *
	 * @param text
	 * @return
	 */
	public List<String> tokenizeText(String text) {
		List<String> tokenizedWords = new ArrayList<String>();
		String[] words = text.split("\\s");
		Pattern pattern = Pattern.compile(matchPattern, Pattern.DOTALL | Pattern.MULTILINE);

		for (String word : words) {
			// If the term is empty, skip it, since we probably just have multiple whitespace characters.
			if (StringUtils.isNullOrEmpty(word))
				continue;
			// Now, a word can be preceded or succeeded by symbols, so let's split those out
			Matcher matcher = pattern.matcher(word);
			if (!matcher.find()) {
				tokenizedWords.add(word);
				continue;
			}
			for (int i = 1; i <= matcher.groupCount(); i++) {
				if (!StringUtils.isNullOrEmpty(matcher.group(i))) {
					logger.debug("Matcher group : " + i + " text :  " + matcher.group(i));
					tokenizedWords.add(matcher.group(i));
				}
			}
		}

		return tokenizedWords;

	}
}
