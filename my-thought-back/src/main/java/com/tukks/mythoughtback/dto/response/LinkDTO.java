package com.tukks.mythoughtback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LinkDTO {

	private String domain;

	private String url;

	private String title;

	private String desc;

	private String image;

	private String imageAlt;
}