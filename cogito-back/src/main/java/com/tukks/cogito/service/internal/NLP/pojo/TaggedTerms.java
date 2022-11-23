package com.tukks.cogito.service.internal.NLP.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Tagged terms class
 *
 * @author sree
 */

@Builder
@Getter
@Setter
public class TaggedTerms {

	public String term;
	public String tag;
	public String norm;

	public TaggedTerms(String term, String tag, String norm) {
		this.term = term;
		this.tag = tag;
		this.norm = norm;
	}

}
