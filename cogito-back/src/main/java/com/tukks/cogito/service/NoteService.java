package com.tukks.cogito.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.tukks.cogito.dto.ThingType;
import com.tukks.cogito.dto.request.TagEditRequest;
import com.tukks.cogito.dto.request.ThingsEditRequest;
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

@Service
@AllArgsConstructor
public class NoteService {

	private final LinkPreview linkPreview;
	private final LinkRepository linkRepository;

	private final NoteRepository noteRepository;
	private final ThingsRepository thingsRepository;
	private final TweetService tweetService;

	private static final String REGEX_TWITTER = "^https?:\\/\\/twitter\\.com\\/(?:#!\\/)?(\\w+)\\/status(es)?\\/(\\d+)";

	public void save(final String note) {
		final String noteCleaned = note.trim();
		if (isTwitterUrl(noteCleaned)) {
			tweetService.addTweet(noteCleaned);
		} else if (isValidURL(noteCleaned)) {
			final LinkEntity linkEntity = linkPreview.extractLinkPreviewInfo(noteCleaned);
			linkEntity.setThingType(ThingType.LINK);
			linkEntity.setOidcSub(getSub());
			linkRepository.save(linkEntity);
		} else {
			final NoteEntity noteEntity = new NoteEntity(note);
			noteEntity.setThingType(ThingType.MARKDOWN);
			noteEntity.setOidcSub(getSub());
			noteRepository.save(noteEntity);
		}
	}

	@Transactional
	public void delete(final Long id) {
		thingsRepository.deleteByIdAndOidcSub(id, getSub());
	}

	@Transactional
	public void editThings(final Long id, final ThingsEditRequest thingsEditRequest) {
		final String sub = getSub();
		ThingsEntity thingsEntity = thingsRepository.getByIdAndOidcSub(id, sub);
		if (thingsEntity == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem with id");
		}
		if (thingsEntity.getThingType() == ThingType.MARKDOWN) {
			NoteEntity noteEntity = noteRepository.getByIdAndOidcSub(id, sub);
			noteEntity.setMarkdown(thingsEditRequest.getNote());
			if (thingsEditRequest.getTags() != null) {
				noteEntity.setTags(createTagsEntityFromString(thingsEditRequest.getTags()));
			}
			if (thingsEditRequest.getComment() != null) {
				noteEntity.setComment(thingsEditRequest.getComment());
			}
			if (thingsEditRequest.getTitle() != null) {
				noteEntity.setTitle(thingsEditRequest.getTitle());
			}
			noteRepository.save(noteEntity);
		} else {
			if (thingsEditRequest.getTitle() != null) {
				thingsEntity.setTitle(thingsEditRequest.getTitle());
			}
			if (thingsEditRequest.getTags() != null) {
				thingsEntity.setTags(createTagsEntityFromString(thingsEditRequest.getTags()));
			}
			if (thingsEditRequest.getComment() != null) {
				thingsEntity.setComment(thingsEditRequest.getComment());
			}
			thingsRepository.save(thingsEntity);
		}

	}

	private List<Tag> createTagsEntityFromString(List<TagEditRequest> tags) {
		return tags.stream().map(s -> {
			Tag newTag = new Tag();
			newTag.setTag(s.getTag());
			return newTag;
		}).collect(Collectors.toList());
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
