package com.tukks.cogito.service.internal.NLP;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import com.github.pemistahl.lingua.api.IsoCode639_1;
import com.github.pemistahl.lingua.api.Language;
import com.tukks.cogito.configuration.NLP.EnglishMaxentTagger;
import com.tukks.cogito.configuration.NLP.FrenchMaxentTagger;
import com.tukks.cogito.service.internal.NLP.pojo.TaggedTermsContainer;
import com.tukks.cogito.service.internal.NLP.pojo.TermDocument;
import com.tukks.cogito.service.internal.languagedetector.LanguageDetector;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaggerNLP {

	private final Logger logger = LogManager.getLogger(getClass());

	private final FrenchMaxentTagger frenchMaxentTagger;
	private final EnglishMaxentTagger englishMaxentTagger;

	private final DefaultTagger defaultTagger;

	private final TextCleaner textCleaner;

	private final LanguageDetector languageDetector;

	public TermDocument getTagFromNlp(String text) {
		TermDocument termDocument = new TermDocument();

		termDocument.setNormalizedText(textCleaner.normalizeText(text));
		termDocument.setTerms(textCleaner.tokenizeText(termDocument.getNormalizedText()));

		Language language = languageDetector.detectLanguage(text);
		logger.info("Language detected : {}", language.getIsoCode639_1());
		termDocument = tag(language, termDocument);

		TermExtractor termExtractor = new TermExtractor();
		termDocument.setExtractedTerms(termExtractor.extractTerms(termDocument.getTaggedContainer()));

		TermsFilter termsFilter = new TermsFilter(3, 2);
		termDocument.setFinalFilteredTerms(termsFilter.filterTerms(termDocument.getExtractedTerms()));

		return termDocument;
	}

	private TermDocument tag(Language language, TermDocument termDocument) {
		TaggedTermsContainer taggedTermsContainer = new TaggedTermsContainer();

		if (language.getIsoCode639_1() == IsoCode639_1.FR) {
			extractTagFrench(termDocument, taggedTermsContainer);
		} else {
			extractTagEnglish(termDocument, taggedTermsContainer);
		}

		termDocument.setTaggedContainer(taggedTermsContainer);
		termDocument = defaultTagger.postTagProcess(termDocument);

		return termDocument;
	}

	private void extractTagFrench(TermDocument termDocument, TaggedTermsContainer taggedTermsContainer) {
		for (String term : termDocument.getTerms()) {
			String tag = frenchMaxentTagger.tagTokenizedString(term);
			// Since Stanford POS has tagged terms like establish_VB , we only need the POS tag
			// Some POS tags in Stanford has special charaters at end like their/PRP$
			taggedTermsContainer.addTaggedTerms(term, tag.split("_")[1].replaceAll("\\$", ""), term);

		}
	}

	private void extractTagEnglish(TermDocument termDocument, TaggedTermsContainer taggedTermsContainer) {
		for (String term : termDocument.getTerms()) {
			String tag = englishMaxentTagger.tagTokenizedString(term);
			// Since Stanford POS has tagged terms like establish_VB , we only need the POS tag
			// Some POS tags in Stanford has special charaters at end like their/PRP$
			taggedTermsContainer.addTaggedTerms(term, tag.split("_")[1].replaceAll("\\$", ""), term);

		}
	}

}
