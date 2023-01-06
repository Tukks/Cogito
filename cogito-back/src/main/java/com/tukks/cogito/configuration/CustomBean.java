package com.tukks.cogito.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tukks.cogito.configuration.NLP.EnglishMaxentTagger;
import com.tukks.cogito.configuration.NLP.FrenchMaxentTagger;

import edu.stanford.nlp.util.StringUtils;

@Configuration
public class CustomBean {

	@Bean
	EnglishMaxentTagger englishMaxenTagger() {
		return new EnglishMaxentTagger("nlp-model/english-left3words-distsim.tagger");
	}

	@Bean
	FrenchMaxentTagger frenchMaxenTagger() {
		Properties props = StringUtils.argsToProperties("-props", "french");

		return new FrenchMaxentTagger("nlp-model/french-ud.tagger", props);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.findAndRegisterModules();
		return objectMapper;
	}
}
