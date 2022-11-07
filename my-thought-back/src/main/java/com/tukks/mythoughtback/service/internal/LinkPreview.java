package com.tukks.mythoughtback.service.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tukks.mythoughtback.entity.LinkEntity;
import com.tukks.mythoughtback.entity.tag.Tag;

/**
 * utilisé crux ?? https://github.com/chimbori/crux
 */
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
			String title = getTitle(document);
			String desc = getDescription(document);
			String ogUrl = StringUtils.defaultIfBlank(getUrl(document), url);
			String ogImage = getImg(document, url);
			String ogImageAlt = getMetaTagContent(document, "meta[property=og:image:alt]");
			Boolean isArticle = isArticle(document);
			String domain = new URL(ogUrl).getHost();
			LinkEntity linkEntity = new LinkEntity(domain, url, title, desc, ogImage, ogImageAlt);

			if (isArticle) {
				Tag tag = new Tag();
				tag.setTag("read later");
				linkEntity.setTags(List.of(tag));
			}
			return linkEntity;

		} catch (IOException e) {
			logger.warn("Unable to connect to extract domain name from : {}", url);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem parsing the link");
		}
	}

	/**
	 * determine si un lien et un site de news
	 * pas sur que ça fonctionne comme ça
	 *
	 * @param document
	 * @return
	 */
	private Boolean isArticle(Document document) {
		Element article = document.select("article").first();
		if (article != null) {
			return true;
		}
		return false;
	}

	private String getTitle(Document document) {
		var title = getMetaTagContent(document, "meta[property=\"og:title\"]");
		if (!title.equals(StringUtils.EMPTY)) {
			return title;
		}
		title = getMetaTagContent(document, "meta[name=\"twitter:title\"]");
		if (!title.equals(StringUtils.EMPTY)) {
			return title;
		}
		title = document.title();
		if (!title.equals(StringUtils.EMPTY)) {
			return title;
		}
		Element h1 = document.select("h1").first();
		if (h1 != null) {
			return h1.html();
		}
		Element h2 = document.select("h2").first();
		if (h2 != null) {
			return h2.html();
		}
		return "";
	}

	private String getDescription(Document document) {
		var description = getMetaTagContent(document, "meta[property=\"og:description\"]");
		if (!description.equals(StringUtils.EMPTY)) {
			return description;
		}
		description = getMetaTagContent(document, "meta[name=\"twitter:description\"]");
		if (!description.equals(StringUtils.EMPTY)) {
			return description;
		}
		description = getMetaTagContent(document, "meta[name=\"description\"]");
		if (!description.equals(StringUtils.EMPTY)) {
			return description;
		}
		Element p = document.select("p").first();
		if (p != null) {
			return p.html();
		}
		return "";
	}

	private String getUrl(Document document) {
		var url = getMetaTagContent(document, "link[rel=canonical]");
		if (!url.equals(StringUtils.EMPTY)) {
			return url;
		}
		url = getMetaTagContent(document, "meta[property=\"og:url\"]");
		if (!url.equals(StringUtils.EMPTY)) {
			return url;
		}
		return "";
	}

	private String getImg(Document document, String url) {
		var img = getMetaTagContent(document, "meta[property=\"og:image\"]");
		if (!img.equals(StringUtils.EMPTY)) {
			return img;
		}
		img = getMetaTagContent(document, "link[rel=\"image_src\"]");
		if (!img.equals(StringUtils.EMPTY)) {
			return img;
		}
		img = getMetaTagContent(document, "meta[name=\"twitter:image\"]");
		if (!img.equals(StringUtils.EMPTY)) {
			return img;
		}
		Element imgElement = document.select("img").first();
		if (imgElement != null) {
			String src = imgElement.attr("src");
			if (src.contains("//")) {
				return src;
			} else {
				try {
					URL uri = new URL(url);
					return uri.getProtocol() + "://" + uri.getHost() + src;
				} catch (MalformedURLException e) {
					// Pas dramatique, on a rien besoin de faire avec cette exception
					return "";
				}
			}
		}
		return "";
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
