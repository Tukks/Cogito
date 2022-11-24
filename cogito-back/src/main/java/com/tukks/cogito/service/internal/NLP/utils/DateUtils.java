package com.tukks.cogito.service.internal.NLP.utils;

import java.util.Set;

public class DateUtils {

	public static final Set<String> DateOffsets;

	static {
		String[] elements = {"PM", "ET", "UST", "AM", "IST", "PDT", "AD"};
		DateOffsets = Set.of(elements);
	}

	public Set<String> getDateOffsets() {
		return DateOffsets;
	}

}
