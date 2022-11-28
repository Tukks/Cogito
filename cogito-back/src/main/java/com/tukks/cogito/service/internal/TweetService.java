package com.tukks.cogito.service.internal;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import com.tukks.cogito.dto.ThingType;
import com.tukks.cogito.dto.response.TweetDTO;
import com.tukks.cogito.entity.TweetEntity;
import com.tukks.cogito.entity.tag.Tag;
import com.tukks.cogito.repository.TweetRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import static com.tukks.cogito.service.SecurityUtils.getSub;
import static com.tukks.cogito.utils.Constants.REGEX_TWITTER;

@Service
@AllArgsConstructor
@Data
public class TweetService {

	private final Logger logger = LogManager.getLogger(getClass());

	private TweetRepository tweetRepository;

	private TweetPreview tweetPreview;

	public TweetEntity addTweet(String url, List<Tag> tags) {
		Pattern pattern = Pattern.compile(REGEX_TWITTER);
		String cleanedUrl = pattern.matcher(url)
			.results()                       // Stream<MatchResult>
			.map(mr -> mr.group(0)).findFirst().orElseThrow();

		TweetDTO tweetDTO = tweetPreview.extractTweetContent(cleanedUrl);
		TweetEntity tweetEntity = TweetEntity.builder()
			.author(tweetDTO.getAuthor())
			.url(tweetDTO.getUrl())
			.content(tweetDTO.getContent())
			.hashtag(tweetDTO.getHashtag())
			.media(tweetDTO.getMedia())
			.html(tweetDTO.getHtml())
			.build();
		tweetEntity.setThingType(ThingType.TWEET);
		tweetEntity.setTags(List.of(Tag.builder().tag("tweet").build()));
		tweetEntity.setOidcSub(getSub());
		return tweetRepository.save(tweetEntity);
	}

}


