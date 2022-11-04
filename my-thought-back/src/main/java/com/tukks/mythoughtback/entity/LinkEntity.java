package com.tukks.mythoughtback.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@DiscriminatorValue("LINK")
public class LinkEntity extends ThingsEntity {

	@Column(name = "domain")
	private String domain;

	@Column(name = "url")
	private String url;

	@Column(name = "title")
	private String title;

	@Column(name = "desc")
	private String desc;

	@Column(name = "image")
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
