package com.tukks.cogito.service.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.boilerpipe.BoilerpipeContentHandler;

import org.springframework.stereotype.Service;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.tukks.cogito.entity.LinkEntity;
import com.tukks.cogito.entity.tag.Tag;
import com.tukks.cogito.service.internal.NLP.TaggerNLP;
import com.tukks.cogito.service.internal.NLP.pojo.TermDocument;

import de.l3s.boilerpipe.extractors.ArticleExtractor;
import lombok.AllArgsConstructor;

/**
 * utilisé crux ?? https://github.com/chimbori/crux
 * todo make async
 */
@Service
@AllArgsConstructor
public class LinkPreview {

	private record ArticleExtract(String title, String article) {}

	private TaggerNLP taggerNLP;
	private final Logger logger = LogManager.getLogger(getClass());

	/**
	 * Parses the web page and extracts the info from meta tags required for preview
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public LinkEntity extractLinkPreviewInfo(String url) {
		try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			Page htmlPage = webClient.getPage(url);
			Document document = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());

			LinkEntity linkEntity = extractInformationFromWebPage(url, document);
			ArticleExtract articleExtract = getArticleFromHtml(document, linkEntity);

			createTagFromNLPAnalyse(linkEntity, articleExtract);

			return linkEntity;

		} catch (IOException | SAXException | TikaException | FailingHttpStatusCodeException e) {
			logger.warn("Unable to connect to extract domain name from : {}", url);
			// if something not working, with save it as markdnow note
			LinkEntity linkEntity = new LinkEntity();
			linkEntity.setUrl(url);
			linkEntity.setTags(Collections.emptyList());
			return linkEntity;

		}
	}

	private void createTagFromNLPAnalyse(LinkEntity linkEntity, ArticleExtract articleExtract) {
		TermDocument termDocument = taggerNLP.getTagFromNlp(articleExtract.article);

		linkEntity.setTags(getNLPTag(termDocument));
	}

	@NotNull
	private List<Tag> getNLPTag(TermDocument termDocument) {
		List<Tag> tags = new ArrayList<>();
		termDocument.finalFilteredTerms.forEach((s, integers) -> tags.add(Tag.builder().tag(s).hidden(true).build()));
		return tags;
	}

	@NotNull
	private ArticleExtract getArticleFromHtml(Document document, LinkEntity linkEntity) throws IOException, SAXException, TikaException {
		Boolean isArticle = isArticle(document);

		ArticleExtract articleExtract = parseNews(document.html());
		linkEntity.setContent(articleExtract.article);
		linkEntity.setTitle(articleExtract.title);
		return articleExtract;
	}

	@NotNull
	private LinkEntity extractInformationFromWebPage(String url, Document document) throws MalformedURLException {
		String title = getTitle(document);
		String desc = getDescription(url, document);
		String ogUrl = StringUtils.defaultIfBlank(getUrl(document), url);
		String ogImage = getImg(document, url);
		String ogImageAlt = getMetaTagContent(document, "meta[property=og:image:alt]");
		String domain = new URL(ogUrl).getHost();
		return new LinkEntity(domain, url, title, desc, ogImage, ogImageAlt);
	}

	public ArticleExtract parseNews(String html) throws IOException, SAXException, TikaException {

		final InputStream input = new ByteArrayInputStream(html.getBytes());
		final BodyContentHandler bodyContentHandler = new BodyContentHandler();
		final ArticleExtractor articleExtractor = new ArticleExtractor();
		final BoilerpipeContentHandler textHandler = new BoilerpipeContentHandler(bodyContentHandler, articleExtractor);
		final Metadata metadata = new Metadata();
		final HtmlParser parser = new HtmlParser();
		final ParseContext context = new ParseContext();

		parser.parse(input, textHandler, metadata, context);

		String contentEncoding = metadata.get(Metadata.CONTENT_ENCODING);
		if (contentEncoding != null && !contentEncoding.equals(StandardCharsets.UTF_8.name())) {
			return new ArticleExtract(new String(textHandler.getTitle().getBytes(contentEncoding), StandardCharsets.UTF_8),
				new String(bodyContentHandler.toString().getBytes(contentEncoding), StandardCharsets.UTF_8));
		}
		return new ArticleExtract(textHandler.getTitle(), bodyContentHandler.toString());
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
		return article != null;
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

	private String getDescription(String url, Document document) {
		if (url.contains("wikipedia")) {
			for (Element element : document.select(".mw-content-ltr p")) {
				if (!element.text().equals(StringUtils.EMPTY)) {
					return element.text();
				}
			}
		}
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
					return uri.getProtocol() + "://" + uri.getHost() + '/' + src;
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
