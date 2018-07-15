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
import java.util.stream.Collectors;

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

    private static final String spinoffSelector = "td[class='ar fw-n borderClass']:contains(Spin-off:)";
    private static final String adaptationSelector = "td[class='ar fw-n borderClass']:contains(Adaptation:)";
    private static final String prequelSelector = "td[class='ar fw-n borderClass']:contains(Prequel:)";
    private static final String sequelSelector = "td[class='ar fw-n borderClass']:contains(Sequel:)";
    private static final String otherSelector = "td[class='ar fw-n borderClass']:contains(Other:)";
    private static final String sideStorySelector = "td[class='ar fw-n borderClass']:contains(Side story:)";
    private static final String altSettingSelector = "td[class='ar fw-n borderClass']:contains(Alternative setting:)";
    private static final String altVersionSelector = "td[class='ar fw-n borderClass']:contains(Alternative version:)";
    private static final String summarySelector = "td[class='ar fw-n borderClass']:contains(Summary:)";
    private static final String parentStorySelector = "td[class='ar fw-n borderClass']:contains(Parent story:)";

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
     * Returns a list of genres from the 'Genres' row
     * in the 'Information' section
     */
    public List<String> parseGenres() {
        Element elem = parent(genreSelector);
        List<String> genres = new ArrayList<>();

        if (elem == null || elem.text().contains("None found,")) {
            return genres;
        }

        for (Element href : elem.children()) {
            if (!href.tagName().equals("a")) {
                continue;
            }

            genres.add(href.ownText());
        }

        return genres;
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

    /*
     * Returns a list of paths from the 'Adaptation' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseAdaptations() {
        return parseRelated(adaptationSelector);
    }

    /*
     * Returns a list of paths from the 'Prequel' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parsePrequels() {
        return parseRelated(prequelSelector);
    }

    /*
     * Returns a list of paths from the 'Sequel' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseSequels() {
        return parseRelated(sequelSelector);
    }

    /*
     * Returns a list of paths from the 'Spin-off' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseSpinoffs() {
        return parseRelated(spinoffSelector);
    }

    /*
     * Returns a list of paths from the 'Side story' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseSideStories() {
        return parseRelated(sideStorySelector);
    }

    /*
     * Returns a list of paths from the 'Alternative setting' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseAlternativeSettings() {
        return parseRelated(altSettingSelector);
    }

    /*
     * Returns a list of paths from the 'Alternative version' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseAlternativeVersions() {
        return parseRelated(altVersionSelector);
    }

    /*
     * Returns a list of paths from the 'Summary' row
     * in the 'Related Anime' section
     */
    public List<String> parseSummaries() {
        return parseRelated(summarySelector);
    }

    /*
     * Returns a list of paths from the 'Parent story' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseParentStories() {
        return parseRelated(parentStorySelector);
    }

    /*
     * Returns a list of paths from the 'Other' row
     * in the 'Related Anime/Manga' section
     */
    public List<String> parseOthers() {
        return parseRelated(otherSelector);
    }

    /*
     * Returns a list of paths from the selected
     * row in the 'Related Anime/Manga' section
     */
    private List<String> parseRelated(String relatedSelector) {
        Element related = nextElementSibling(relatedSelector);
        if (related == null) {
            return new ArrayList<>();
        }

        return related.children().stream()
                .map(elem -> elem.attr("href").substring(1)) // Trim the beginning slash
                .collect(Collectors.toList());
    }
}
