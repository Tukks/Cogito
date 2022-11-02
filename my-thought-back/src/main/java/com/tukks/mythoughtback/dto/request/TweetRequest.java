package com.tukks.mythoughtback.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class TweetRequest {

	private String url;
}
