package com.tukks.mythoughtback.service.internal;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tukks.mythoughtback.entity.LinkEntity;

@Service
public class LinkPreview {

	private final Logger logger = LogManager.getLogger(getClass());

	/**
	 * Parses the web page and extracts the info from meta tags required for preview
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public LinkEntity extractLinkPreviewInfo(String url) {
		try {
			if (!url.startsWith("http")) {
				url = "http://" + url;
			}
			Document document = Jsoup.connect(url)
				.userAgent("Mozilla")
				.get();
			String title = getMetaTagContent(document, "meta[name=title]");
			String desc = getMetaTagContent(document, "meta[name=description]");
			String ogUrl = StringUtils.defaultIfBlank(getMetaTagContent(document, "meta[property=og:url]"), url);
			String ogTitle = getMetaTagContent(document, "meta[property=og:title]");
			String ogDesc = getMetaTagContent(document, "meta[property=og:description]");
			String ogImage = getMetaTagContent(document, "meta[property=og:image]");
			String ogImageAlt = getMetaTagContent(document, "meta[property=og:image:alt]");
			String domain = ogUrl;

			domain = new URL(ogUrl).getHost();
			return new LinkEntity(domain, url, StringUtils.defaultIfBlank(ogTitle, title), StringUtils.defaultIfBlank(ogDesc, desc), ogImage, ogImageAlt);

		} catch (IOException e) {
			logger.warn("Unable to connect to extract domain name from : {}", url);
		}
		return null;
	}

	/**
	 * Returns the given meta tag content
	 *
	 * @param document
	 * @return
	 */
	private String getMetaTagContent(Document document, String cssQuery) {
		Element elm = document.select(cssQuery).first();
		if (elm != null) {
			return elm.attr("content");
		}
		return "";
	}
}
