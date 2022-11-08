package com.tukks.mythoughtback.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tukks.mythoughtback.dto.ThingType;
import com.tukks.mythoughtback.dto.request.TagEditRequest;
import com.tukks.mythoughtback.dto.request.ThingsEditRequest;
import com.tukks.mythoughtback.entity.LinkEntity;
import com.tukks.mythoughtback.entity.NoteEntity;
import com.tukks.mythoughtback.entity.ThingsEntity;
import com.tukks.mythoughtback.entity.tag.Tag;
import com.tukks.mythoughtback.repository.LinkRepository;
import com.tukks.mythoughtback.repository.NoteRepository;
import com.tukks.mythoughtback.repository.TagRepository;
import com.tukks.mythoughtback.repository.ThingsRepository;
import com.tukks.mythoughtback.service.internal.LinkPreview;
import com.tukks.mythoughtback.service.internal.TweetService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoteService {

	private final LinkPreview linkPreview;
	private final LinkRepository linkRepository;

	private final NoteRepository noteRepository;
	private final ThingsRepository thingsRepository;
	private final TweetService tweetService;
	private final TagRepository tagRepository;

	private final String REGEX_TWITTER = "^https?:\\/\\/twitter\\.com\\/(?:#!\\/)?(\\w+)\\/status(es)?\\/(\\d+)";

	public void save(final String note) {
		final String noteCleaned = note.trim();
		// détermine le type de note crée, on pourrait ajouter les images, instagram
		// si le texte a des quotes, on considére que c'est une citation
		// bug avec recherche google??
		if (isTwitterUrl(noteCleaned)) {
			tweetService.addTweet(noteCleaned);
			// do something
		} else if (isValidURL(noteCleaned)) {
			final LinkEntity linkEntity = linkPreview.extractLinkPreviewInfo(noteCleaned);
			linkEntity.setThingType(ThingType.LINK);
			linkRepository.save(linkEntity);
		} else {
			// On considere que c'est du MD classique
			final NoteEntity noteEntity = new NoteEntity(note);
			noteEntity.setThingType(ThingType.MARKDOWN);
			noteRepository.save(noteEntity);
		}
	}

	@Transactional
	public void delete(final Long id) {
		thingsRepository.deleteById(id);
	}

	// TODO corriger la sauvegarde des tags en duplicate
	@Transactional
	public void editThings(final Long id, final ThingsEditRequest thingsEditRequest) {
		ThingsEntity thingsEntity = thingsRepository.getById(id);

		if (thingsEntity.getThingType() == ThingType.MARKDOWN) {
			NoteEntity noteEntity = noteRepository.getById(id);
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
			//			Tag existTag = tagRepository.getTagByTag(s.getTag());
			//			if (existTag != null) {
			//				return existTag;
			//			}
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
