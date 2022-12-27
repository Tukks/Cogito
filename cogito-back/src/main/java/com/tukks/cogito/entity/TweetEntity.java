package com.tukks.cogito.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorValue("TWEET")
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@Builder
public class TweetEntity extends ThingsEntity {

	private String url;

	@Lob
	private String content;

	private String media;

	private String author;

	private String hashtag;

	@Lob
	private String html;
}
