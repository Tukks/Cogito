package com.tukks.cogito.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.tukks.cogito.dto.ThingType;
import com.tukks.cogito.dto.request.TagEditRequest;
import com.tukks.cogito.dto.request.ThingsRequest;
import com.tukks.cogito.entity.LinkEntity;
import com.tukks.cogito.entity.NoteEntity;
import com.tukks.cogito.entity.ThingsEntity;
import com.tukks.cogito.entity.tag.Tag;
import com.tukks.cogito.repository.LinkRepository;
import com.tukks.cogito.repository.NoteRepository;
import com.tukks.cogito.repository.ThingsRepository;
import com.tukks.cogito.service.internal.LinkPreview;
import com.tukks.cogito.service.internal.TweetService;

import lombok.AllArgsConstructor;
import static com.tukks.cogito.service.SecurityUtils.getSub;
import static com.tukks.cogito.utils.Constants.REGEX_TWITTER;

@Service
@AllArgsConstructor
public class NoteService {

	private final Logger logger = LogManager.getLogger(getClass());
	private final LinkPreview linkPreview;
	private final LinkRepository linkRepository;

	private final NoteRepository noteRepository;
	private final ThingsRepository thingsRepository;
	private final TweetService tweetService;


	public Object save(final ThingsRequest thingsRequest) {
		final String noteCleaned = thingsRequest.getNote().trim();
		if (isTwitterUrl(noteCleaned)) {
			logger.info("Added Tweet note");
			return tweetService.addTweet(noteCleaned, createTagsEntityFromString(thingsRequest.getTags()));
		} else if (isValidURL(noteCleaned)) {
			logger.info("Added Link note");

			final LinkEntity linkEntity = linkPreview.extractLinkPreviewInfo(noteCleaned);
			linkEntity.setThingType(ThingType.LINK);
			linkEntity.setOidcSub(getSub());
			linkEntity.setTags(createTagsEntityFromString(thingsRequest.getTags()));

			return linkRepository.save(linkEntity);
		} else {
			logger.info("Added note");

			final NoteEntity noteEntity = new NoteEntity(thingsRequest.getNote());
			noteEntity.setThingType(ThingType.MARKDOWN);
			noteEntity.setOidcSub(getSub());
			noteEntity.setTags(createTagsEntityFromString(thingsRequest.getTags()));
			return noteRepository.save(noteEntity);
		}
	}

	@Transactional
	public Integer delete(final UUID id) {
		return thingsRepository.deleteByIdAndOidcSub(id, getSub());
	}

	@Transactional
	public Object editThings(final UUID id, final ThingsRequest thingsRequest) {
		final String sub = getSub();
		ThingsEntity thingsEntity = thingsRepository.getByIdAndOidcSub(id, sub);
		if (thingsEntity == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem with id");
		}
		if (thingsEntity.getThingType() == ThingType.MARKDOWN) {
			NoteEntity noteEntity = noteRepository.getByIdAndOidcSub(id, sub);
			noteEntity.setMarkdown(thingsRequest.getNote());
			if (thingsRequest.getTags() != null) {
				noteEntity.setTags(createTagsEntityFromString(thingsRequest.getTags()));
			}
			if (thingsRequest.getComment() != null) {
				noteEntity.setComment(thingsRequest.getComment());
			}
			if (thingsRequest.getTitle() != null) {
				noteEntity.setTitle(thingsRequest.getTitle());
			}
			return noteRepository.save(noteEntity);
		} else {
			if (thingsRequest.getTitle() != null) {
				thingsEntity.setTitle(thingsRequest.getTitle());
			}
			if (thingsRequest.getTags() != null) {
				thingsEntity.setTags(createTagsEntityFromString(thingsRequest.getTags()));
			}
			if (thingsRequest.getComment() != null) {
				thingsEntity.setComment(thingsRequest.getComment());
			}
			return thingsRepository.save(thingsEntity);
		}

	}

	private List<Tag> createTagsEntityFromString(List<TagEditRequest> tags) {
		if (tags != null) {
			return tags.stream().map(s -> {
				Tag newTag = new Tag();
				newTag.setTag(s.getTag());
				newTag.setHidden(s.isHidden());
				return newTag;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public boolean isTwitterUrl(String url) {
		final Pattern patternTwitter = Pattern.compile(REGEX_TWITTER);
		final Matcher matcherTwitter = patternTwitter.matcher(url);
		return matcherTwitter.find();
	}

	public boolean isValidURL(String url) {

		try {
			new URL(url).toURI();
		} catch (MalformedURLException | URISyntaxException e) {
			return false;
		}

		return true;
	}
}
