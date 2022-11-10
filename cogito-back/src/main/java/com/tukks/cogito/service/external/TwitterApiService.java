package com.tukks.cogito.service.external;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.tukks.cogito.dto.response.TweetDTO;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.auth.TwitterOAuth20AppOnlyService;
import com.twitter.clientlib.model.Get2TweetsIdResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;

@Service
@PropertySource("application-secret.properties")
public class TwitterApiService {

	Logger logger = LoggerFactory.getLogger(TwitterApiService.class);
	@Value("${api.twitter.key}")
	private String TWITTER_KEY;
	@Value("${api.twitter.key.secret}")
	private String TWITTER_SECRET;

	private OAuth2AccessToken getAccessToken() {
		TwitterOAuth20AppOnlyService service = new TwitterOAuth20AppOnlyService(TWITTER_KEY, TWITTER_SECRET);
		OAuth2AccessToken accessToken = null;
		try {
			accessToken = service.getAccessTokenClientCredentialsGrant();
			logger.debug("Access token: " + accessToken.getAccessToken());
			logger.debug("Token type: " + accessToken.getTokenType());
		} catch (Exception e) {
			logger.error("Error while getting the access token:\n " + e);
			e.printStackTrace();
		}
		return accessToken;
	}

	public TweetDTO getTweetFields(String url) {
		TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(getAccessToken().getAccessToken());
		TwitterApi apiInstance = new TwitterApi(credentials);

		Set<String> tweetFields = new HashSet<>();
		tweetFields.add("author_id");
		tweetFields.add("id");
		tweetFields.add("created_at");
		tweetFields.add("text");
		tweetFields.add("attachments");
		TweetDTO tweetDTO = new TweetDTO();
		try {
			// findTweetById
			String tweetId = getTweetIdByUrl(url, tweetDTO);
			Get2TweetsIdResponse result = apiInstance
				.tweets()
				.findTweetById(tweetId)
				.tweetFields(tweetFields)
				.execute();
			if (result.getErrors() != null && result.getErrors().size() > 0) {

				result.getErrors().forEach(e -> {
					logger.error("error : {}", e);

					if (e instanceof ResourceUnauthorizedProblem) {

						logger.error(e.getTitle() + " " + e.getDetail());
					}
				});
			} else {
				tweetDTO.setUrl(url);
				tweetDTO.setContent(result.getData().getText());
			}
		} catch (ApiException e) {
			logger.error("Status code: " + e.getCode());
			logger.error("Reason: " + e.getResponseBody());
			logger.error("Response headers: " + e.getResponseHeaders(), e);

		}

		return tweetDTO;
	}

	/**
	 * Extract data from URL
	 *
	 * @param url
	 * @param tweetDTO
	 */
	private String getTweetIdByUrl(String url, TweetDTO tweetDTO) {
		StringTokenizer stringTokenizer = new StringTokenizer(url, "/");
		stringTokenizer.nextToken();
		stringTokenizer.nextToken();
		tweetDTO.setAuthor(stringTokenizer.nextToken());
		stringTokenizer.nextToken();
		return stringTokenizer.nextToken();
	}
}
