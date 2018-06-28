package scrapers.abstractScrapers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimeAndMangaDetailsPage extends MalPage {
    private static final String typeSelector = "span[class='dark_text']:contains(Type:)";
    private static final String statusSelector = "span[class='dark_text']:contains(Status:)";
    private static final String genreSelector = "span[class='dark_text']:contains(Genres:)";
    private static final String synopsisSelector = "span[itemprop='description']";
    private static final String backgroundSelector = "h2[style='margin-top: 15px;']:contains(Background)";

    public AnimeAndMangaDetailsPage(Document doc) {
        super(doc);
    }

    public String parseTitle() {
        return StringUtils.removeEnd(doc.title(), " - MyAnimeList.net");
    }

    public String parseType() {
        Element type = nextElementSibling(typeSelector);
        return ownText(type);
    }

    public String parseStatus() {
        Element status = parent(statusSelector);
        return ownText(status);
    }

    public List<Integer> parseIds(String selector, Pattern pattern) {
        Element elem = parent(selector);
        List<Integer> ids = new ArrayList<>();

        if (elem == null || elem.text().contains("None found,")) {
            return ids;
        }

        for (Element href : elem.children()) {
            if (!href.tagName().equals("a")) {
                continue;
            }

            String path = href.attr("href");
            Matcher producerMatcher = pattern.matcher(path);
            producerMatcher.find();

            int id = Integer.parseInt(producerMatcher.group(1));
            ids.add(id);
        }

        return ids;
    }

    public List<Integer> parseGenres() {
        return parseIds(genreSelector, genreIdPattern);
    }

    public String parseSynopsis() {
        Element synopsis = doc.selectFirst(synopsisSelector);
        return html(synopsis);
    }

    /*
     * Helper method for parseBackground(). Returns true
     * if the node is either a TextNode, or isn't associated
     * with a div/h2 tag
     */
    private boolean inBackground(Node node) {
        boolean isText = node instanceof TextNode;
        boolean isDivOrH2 = node instanceof Element &&
                (((Element)node).tagName().equals("h2") || ((Element)node).tagName().equals("div"));

        return isText || !isDivOrH2;
    }

    /*
     * Returns the text in the Backgound section of the
     * Anime/Manga main page
     */
    public String parseBackground() {
        Element background = doc.selectFirst(backgroundSelector);
        Node sibling = background.nextSibling();

        StringBuilder backgroundText = new StringBuilder();

        // Loop through the background and grab the text in
        // its entirety. We may have to loop through multiple
        // nodes because of things such as line breaks and
        // italics tags
        while (inBackground(sibling)) {
            backgroundText.append(sibling.toString());
            sibling = sibling.nextSibling();
        }

        return backgroundText.toString();
    }
}
