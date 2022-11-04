package com.tukks.mythoughtback.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class ThingsEditRequest {

	private String title;

	private String note; // Only for markdown

	private List<String> tags;

	private String comment;

}
