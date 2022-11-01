package com.tukks.mythoughtback.service.internal;

import org.springframework.stereotype.Service;

@Service
public class TweetPreview {
	//
	//	https://publish.twitter.com/oembed?
	//	url=https://twitter.com/Interior/status/463440424141459456
	//
	//	public LinkEntity extractLinkPreviewInfo(String tweetUrl) {
	//		try {
	//			if (!url.startsWith("http")) {
	//				url = "http://" + url;
	//			}
	//			Document document = Jsoup.connect("https://publish.twitter.com/oembed?url=" + tweetUrl);
	//			url=https://twitter.com/Interior/status/463440424141459456").get();
	//			String title = getMetaTagContent(document, "meta[name=title]");
	//			String desc = getMetaTagContent(document, "meta[name=description]");
	//			String ogUrl = StringUtils.defaultIfBlank(getMetaTagContent(document, "meta[property=og:url]"), url);
	//			String ogTitle = getMetaTagContent(document, "meta[property=og:title]");
	//			String ogDesc = getMetaTagContent(document, "meta[property=og:description]");
	//			String ogImage = getMetaTagContent(document, "meta[property=og:image]");
	//			String ogImageAlt = getMetaTagContent(document, "meta[property=og:image:alt]");
	//			String domain = ogUrl;
	//
	//			domain = new URL(ogUrl).getHost();
	//			return new LinkEntity(domain, url, StringUtils.defaultIfBlank(ogTitle, title), StringUtils.defaultIfBlank(ogDesc, desc), ogImage, ogImageAlt);
	//
	//		} catch (IOException e) {
	//			logger.warn("Unable to connect to extract domain name from : {}", url);
	//		}
	//		return null;
	//	}
}
