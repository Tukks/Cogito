package com.tukks.cogito.configuration.NLP;

import java.util.Properties;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class FrenchMaxentTagger extends MaxentTagger {

	public FrenchMaxentTagger(String modelFile, Properties props) {
		super(modelFile, props);

	}
}
