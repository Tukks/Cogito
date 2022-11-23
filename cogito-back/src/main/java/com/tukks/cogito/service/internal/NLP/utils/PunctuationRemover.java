package com.tukks.cogito.service.internal.NLP.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Remove punctuation in a given sentence as parameter
 *
 * @param sentence
 * @return string without punctuation
 */

public class PunctuationRemover {

	public static String remove(String sentence) {

		Pattern p = Pattern.compile("^\\W*(.*?)\\W*$");
		Matcher m = p.matcher(sentence);
		if (m.matches()) {
			return m.group(1);
		}
		return sentence;
	}
}