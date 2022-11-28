package com.tukks.cogito.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class ThingsRequest {

	private String title;

	private String note; // Only for markdown

	private List<TagEditRequest> tags;

	private String comment;

}
