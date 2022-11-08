package com.tukks.mythoughtback.service.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tukks.mythoughtback.dto.response.TweetDTO;
import com.tukks.mythoughtback.service.external.TwitterApiService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TweetPreview {

	public record TweetOEmbed(String url, String html) {}

	private TwitterApiService twitterApiService;

	private final Logger logger = LogManager.getLogger(getClass());

	public TweetDTO extractTweetContent(String tweetUrl) {

		TweetDTO tweetDTO = twitterApiService.getTweetFields(tweetUrl);

		Optional<TweetOEmbed> tweetOEmbed = getOEmbedTweet(tweetUrl);
		tweetOEmbed.ifPresent(oEmbed -> tweetDTO.setHtml(oEmbed.html()));

		return tweetDTO;

	}

	private Optional<TweetOEmbed> getOEmbedTweet(String tweetUrl) {
		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "https://publish.twitter.com/oembed?&url=" + tweetUrl;
		URI uri;
		try {
			uri = new URI(baseUrl);

			ResponseEntity<TweetOEmbed> result = restTemplate.getForEntity(uri, TweetOEmbed.class);
			if (result != null) {
				return Optional.of(result.getBody());
			}

			return Optional.empty();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
