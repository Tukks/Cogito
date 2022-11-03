package com.tukks.mythoughtback.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.dto.ThingType;
import com.tukks.mythoughtback.entity.LinkEntity;
import com.tukks.mythoughtback.entity.NoteEntity;
import com.tukks.mythoughtback.repository.LinkRepository;
import com.tukks.mythoughtback.repository.NoteRepository;
import com.tukks.mythoughtback.service.internal.LinkPreview;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaveNote {

	private final LinkPreview linkPreview;
	private final LinkRepository linkRepository;

	private final NoteRepository noteRepository;
	private final String REGEX_TWITTER = "^https?:\\/\\/twitter\\.com\\/(?:#!\\/)?(\\w+)\\/status(es)?\\/(\\d+)";

	public void save(final String note) {
		final String noteCleaned = note.trim();
		// détermine le type de note crée, on pourrait ajouter les images, instagram
		// si le texte a des quotes, on considére que c'est une citation
		// bug avec recherche google??
		if (isTwitterUrl(noteCleaned)) {
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
