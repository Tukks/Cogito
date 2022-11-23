package com.tukks.cogito.service.internal.NLP.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sree
 */

public class TermDocument {

	//List of terms from input text
	public List<String> terms = new ArrayList<String>();

	//terms and its tag from the POS tagger
	public LinkedHashMap<String, String> tagsByTerm = new LinkedHashMap<String, String>();

	//Normalized text
	public String normalizedText;

	//A container for taggedterms
	public TaggedTermsContainer taggedContainer = new TaggedTermsContainer();

	//Extracted terms from the text
	public Map<String, Integer> extractedTerms;

	//Final filteres terms
	public Map<String, ArrayList<Integer>> finalFilteredTerms = new HashMap<String, ArrayList<Integer>>();

	/**
	 * Set terms
	 *
	 * @param terms
	 */
	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	/**
	 * get terms
	 *
	 * @return
	 */
	public List<String> getTerms() {
		return this.terms;
	}

	/**
	 * set tags by term
	 *
	 * @param tagsByTerm
	 */
	public void setTagsByTerm(LinkedHashMap<String, String> tagsByTerm) {
		this.tagsByTerm = tagsByTerm;
	}

	/**
	 * get tags by term
	 *
	 * @return
	 */

	public LinkedHashMap<String, String> getTagsByTerm() {
		return this.tagsByTerm;
	}

	/**
	 * set normalized test
	 *
	 * @param normalizedText
	 */
	public void setNormalizedText(String normalizedText) {
		this.normalizedText = normalizedText;
	}

	/**
	 * get normalized text
	 *
	 * @return
	 */
	public String getNormalizedText() {
		return this.normalizedText;
	}

	/**
	 * set tagged terms container
	 *
	 * @param taggedContainer
	 */
	public void setTaggedContainer(TaggedTermsContainer taggedContainer) {
		this.taggedContainer = taggedContainer;
	}

	/**
	 * get tagged terms container
	 *
	 * @return
	 */
	public TaggedTermsContainer getTaggedContainer() {
		return this.taggedContainer;
	}

	/**
	 * set extracted terms
	 *
	 * @param extractedTerms
	 */
	public void setExtractedTerms(Map<String, Integer> extractedTerms) {
		this.extractedTerms = extractedTerms;
	}

	/**
	 * get extracted terms
	 *
	 * @return
	 */
	public Map<String, Integer> getExtractedTerms() {
		return this.extractedTerms;
	}

	/**
	 * set final filtered terms
	 *
	 * @param filteredTerms
	 */

	public void setFinalFilteredTerms(Map<String, ArrayList<Integer>> filteredTerms) {
		this.finalFilteredTerms = filteredTerms;
	}

	/**
	 * get final filteres terms
	 *
	 * @return
	 */

	public Map<String, ArrayList<Integer>> getFinalFilteredTerms() {
		return this.finalFilteredTerms;
	}
}
