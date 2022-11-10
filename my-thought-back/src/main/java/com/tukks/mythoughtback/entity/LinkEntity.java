package com.tukks.mythoughtback.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

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

	public LinkEntity(String domain, String url, String title, String desc, String ogImage, String ogImageAlt) {

		this.domain = domain;
		this.url = url;
		this.title = title;
		this.desc = desc;
		this.image = ogImage;
		this.imageAlt = ogImageAlt;
	}
}
