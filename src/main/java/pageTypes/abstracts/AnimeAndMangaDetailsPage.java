package pageTypes.abstracts;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Provides HTML scraping utilities for pages of the path 'media/{mediaId}/{mediaTitle}'
 * where media is either 'anime' or 'manga'
 * Example: anime/1735/Naruto__Shippuuden
 */
public class AnimeAndMangaDetailsPage extends MalPage {
    private static final String englishTitleSelector = "span[class='dark_text']:contains(English:)";
    private static final String japaneseTitleSelector = "span[class='dark_text']:contains(Japanese:)";
    private static final String synonymTitleSelector = "span[class='dark_text']:contains(Synonyms:)";
    private static final String typeSelector = "span[class='dark_text']:contains(Type:)";
    private static final String statusSelector = "span[class='dark_text']:contains(Status:)";
    private static final String genreSelector = "span[class='dark_text']:contains(Genres:)";
    private static final String synopsisSelector = "span[itemprop='description']";
    private static final String backgroundSelector = "h2[style='margin-top: 15px;']:contains(Background)";

    public AnimeAndMangaDetailsPage(Document doc) {
        super(doc);
    }

    /*
     * Returns the bolded title right below the
     * top options bar in MyAnimeList
     */
    public String parseMainTitle() {
        return StringUtils.removeEnd(doc.title(), " - MyAnimeList.net");
    }

    /*
     * Returns the title from the 'English' row
     * in the 'Alternative Titles' section
     */
    public String parseEnglishTitle() {
        Element englishTitle = parent(englishTitleSelector);
        return ownText(englishTitle);
    }

    /*
     * Returns the title from the 'Japanese' row
     * in the 'Alternative Titles' section
     */
    public String parseJapaneseTitle() {
        Element japaneseTitle = parent(japaneseTitleSelector);
        return ownText(japaneseTitle);
    }

    /*
     * Returns an array of the titles from the 'Synonyms' row
     * in the 'Alternative Titles' section
     */
    public String[] parseSynonymTitles() {
        Element synonymTitles = parent(synonymTitleSelector);
        String titles = ownText(synonymTitles);
        return titles.equals("") ? new String[0] : titles.split(", ");
    }

    /*
     * Returns the sub-media type from the 'Type' row in
     * the 'Information' section.
     */
    public String parseType() {
        Element type = parent(typeSelector);
        return type.children().size() == 1 ? ownText(type) : ownText(type.child(1));
    }

    /*
     * Returns the completion status from the 'Status' row
     * in the 'Information' section
     */
    public String parseStatus() {
        Element status = parent(statusSelector);
        return ownText(status);
    }

    /*
     * Returns a list of genre ids from the 'Genres' row
     * in the 'Information' section
     */
    public List<Integer> parseGenres() {
        return parseIds(genreSelector, genreIdPattern);
    }

    /*
     * Returns the text in the 'Synopsis' section of the page
     */
    public String parseSynopsis() {
        Element synopsis = doc.selectFirst(synopsisSelector);
        return html(synopsis);
    }

    /*
     * Returns the text in the 'Background' section of the page
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

    // TODO: Refactor this to make it clear what the purpose of this is
    protected List<Integer> parseIds(String selector, Pattern pattern) {
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
}
