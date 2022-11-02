package com.tukks.mythoughtback.service.external;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.auth.TwitterOAuth20AppOnlyService;
import com.twitter.clientlib.model.Get2TweetsIdResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;

import java.util.HashSet;
import java.util.Set;

public class OAuth20AppOnlyGetAccessToken {

    public static void main(String[] args) {
        OAuth2AccessToken accessToken = getAccessToken();
        if (accessToken == null) {
            return;
        }

        // Setting the bearer token into TwitterCredentials
        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(accessToken.getAccessToken());
        callApi(credentials);
    }

    public static OAuth2AccessToken getAccessToken() {
        TwitterOAuth20AppOnlyService service = new TwitterOAuth20AppOnlyService(
                System.getenv("TWITTER_CONSUMER_KEY"),
                System.getenv("TWITTER_CONSUMER_SECRET"));

        OAuth2AccessToken accessToken = null;
        try {
            accessToken = service.getAccessTokenClientCredentialsGrant();

            System.out.println("Access token: " + accessToken.getAccessToken());
            System.out.println("Token type: " + accessToken.getTokenType());
        } catch (Exception e) {
            System.err.println("Error while getting the access token:\n " + e);
            e.printStackTrace();
        }
        return accessToken;
    }

    public static void callApi(TwitterCredentialsBearer credentials) {
        TwitterApi apiInstance = new TwitterApi(credentials);

        Set<String> tweetFields = new HashSet<>();
        tweetFields.add("author_id");
        tweetFields.add("id");
        tweetFields.add("created_at");

        try {
            // findTweetById
            Get2TweetsIdResponse result = apiInstance.tweets().findTweetById("20")
                    .tweetFields(tweetFields)
                    .execute();
            if (result.getErrors() != null && result.getErrors().size() > 0) {
                System.out.println("Error:");
                result.getErrors().forEach(e -> {
                    System.out.println(e.toString());
                    if (e instanceof ResourceUnauthorizedProblem) {
                        System.out.println(
                                ((ResourceUnauthorizedProblem) e).getTitle() + " " + ((ResourceUnauthorizedProblem) e).getDetail());
                    }
                });
            } else {
                System.out.println("findTweetById - Tweet Text: " + result.toString());
            }
        } catch (ApiException e) {
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}