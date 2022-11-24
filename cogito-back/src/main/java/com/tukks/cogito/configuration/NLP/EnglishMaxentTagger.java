package com.tukks.cogito.configuration.NLP;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class EnglishMaxentTagger extends MaxentTagger {

	public EnglishMaxentTagger(String modelFile) {
		super(modelFile);
	}

}
