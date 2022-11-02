package com.tukks.mythoughtback.service;

import com.tukks.mythoughtback.dto.request.TweetRequest;
import com.tukks.mythoughtback.dto.response.TweetDTO;
import com.tukks.mythoughtback.entity.TweetEntity;
import com.tukks.mythoughtback.repository.TweetRepository;
import com.tukks.mythoughtback.service.internal.TweetPreview;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Data
public class TweetService {

    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;
    private TweetRepository tweetRepository;

    @Autowired
    private TweetPreview tweetPreview;

    private static final String REGEX_TWITTER = "^https?:\\/\\/twitter\\.com\\/(?:#!\\/)?(\\w+)\\/status(es)?\\/(\\d+)$";

    public boolean addTweet(TweetRequest tweetRequest) {
        if (isTweet(tweetRequest.getUrl())) {
            TweetDTO tweetDTO = tweetPreview.extractTweetContent(tweetRequest.getUrl());
            tweetRepository.save(modelMapper.map(tweetDTO,TweetEntity.class));
            return Boolean.TRUE;
        }
        logger.debug("URL is not a valid tweet link");
        return Boolean.FALSE;
    }

    public List<TweetDTO> getAll() {
        List<TweetDTO> response = new ArrayList<>();
        for (TweetEntity tweetEntity : tweetRepository.findAll()) {
            response.add(modelMapper.map(tweetEntity, TweetDTO.class));
        }
        return response;
    }

    /**
     * Check if a given link is a tweet or not
     *
     * @param url given link
     * @return true if it's a tweet, false otherwise
     */
    private boolean isTweet(String url) {
        Pattern pattern = Pattern.compile(REGEX_TWITTER);
        return pattern.matcher(url).matches();
    }

    /**
     * Take a valid tweet URL and extract some data to store
     *
     * @return a TweetDTO object with available data
     */
    private String extractDataFromTweetURL(String url) {
        Pattern pattern = Pattern.compile(REGEX_TWITTER);
        Matcher matcher = pattern.matcher(url);
        return url;
    }


}
