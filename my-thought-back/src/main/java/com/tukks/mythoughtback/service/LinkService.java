package com.tukks.mythoughtback.service;

import org.springframework.stereotype.Service;

import com.tukks.mythoughtback.dto.request.LinkRequest;
import com.tukks.mythoughtback.entity.LinkEntity;
import com.tukks.mythoughtback.repository.LinkRepository;
import com.tukks.mythoughtback.service.internal.LinkPreview;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LinkService {

	private final LinkRepository linkRepository;

	private final LinkPreview linkPreview;

	public void addLinkWithPreview(LinkRequest request) {
		linkRepository.save(linkPreview.extractLinkPreviewInfo(request.getUrl()));
	}

	public LinkEntity getPreview(LinkRequest request) {
		return linkPreview.extractLinkPreviewInfo(request.getUrl());
	}
}
