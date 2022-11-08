package com.tukks.mythoughtback.service.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;

import com.tukks.mythoughtback.dto.ThingType;
import com.tukks.mythoughtback.dto.response.TweetDTO;
import com.tukks.mythoughtback.entity.TweetEntity;
import com.tukks.mythoughtback.repository.TweetRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

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

    public boolean addTweet(String url) {
        if (isTweet(url)) {
            TweetDTO tweetDTO = tweetPreview.extractTweetContent(url);
            TweetEntity tweetEntity = TweetEntity.builder()
                .author(tweetDTO.getAuthor())
                .url(tweetDTO.getUrl())
                .content(tweetDTO.getContent())
                .hashtag(tweetDTO.getHashtag())
                .media(tweetDTO.getMedia())
                .build();
            tweetEntity.setThingType(ThingType.TWEET);

            tweetRepository.save(tweetEntity);
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
