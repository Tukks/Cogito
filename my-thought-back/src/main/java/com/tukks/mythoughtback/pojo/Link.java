package com.tukks.mythoughtback.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Link {

	private String domain;

	private String url;

	private String title;

	private String desc;

	private String image;

	private String imageAlt;
}