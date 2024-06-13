package com.tukks.cogito.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Entity
@NoArgsConstructor
@Data
@DiscriminatorValue("LINK")
@EqualsAndHashCode(callSuper = true)
@Jacksonized
public class LinkEntity extends ThingsEntity {

	@Column(name = "domain")
	private String domain;

	@Column(name = "url")
	@Lob
	private String url;

	@Column(name = "desc")
	@Lob
	private String desc;

	@Lob
	private String content;
	@Lob
	private String image;

	@Column(name = "imageAlt")
	private String imageAlt;

	private UUID imageId;

	private String screenshot;

	public LinkEntity(String domain, String url, String title, String desc, String ogImage, String ogImageAlt) {

		this.domain = domain;
		this.url = url;
		this.title = title;
		this.desc = desc;
		this.image = ogImage;
		this.imageAlt = ogImageAlt;
	}
}
