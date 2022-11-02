package com.tukks.mythoughtback.service.internal;

import com.tukks.mythoughtback.dto.response.TweetDTO;
import com.tukks.mythoughtback.service.external.TwitterApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetPreview {

    @Autowired
    private TwitterApiService twitterApiService;

    private final Logger logger = LogManager.getLogger(getClass());

    public TweetDTO extractTweetContent(String tweetUrl) {

        return twitterApiService.getTweetFields(tweetUrl);

    }
}
