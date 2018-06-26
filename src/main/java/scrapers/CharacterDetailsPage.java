package scrapers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import scrapers.abstractScrapers.MalPage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterDetailsPage extends MalPage {
    private final static String nameSelector = "div[style='height: 15px;']";
    private final static String foreignNameSelector = "div[style='height: 15px;']";
    private final static String memberFavoritesSelector = "td:contains(Member Favorites:)";
    private final static String animeographySelector = "div[class='normal_header']:contains(Animeography)";
    private final static String mangaographySelector = "div[class='normal_header']:contains(Mangaography)";
    private final static String voiceActorSelector = "div[class='normal_header']:contains(Voice Actors)";

    public CharacterDetailsPage(Document doc) {
        super(doc);
    }

    /*
     * Returns the character's English name
     */
    public String parseName() {
        Element name = doc.selectFirst(nameSelector);
        return ownText(name);
    }

    /*
     * Returns the character's foreign name. The language depends on
     * where the show was made. Japanese is prominent, followed by
     * Chinese or Korean.
     */
    // TODO: This needs to be revisited. Something seems wonky about the logic here
    public String parseForeignName() {
        Element foreignName = doc.selectFirst(foreignNameSelector);

        if (foreignName == null || foreignName.children().isEmpty()) {
            return "";
        }

        Element span = foreignName.child(0);

        if (span.children().isEmpty()) {
            return null;
        }

        String foreignNameText = span.child(0).ownText();
        foreignNameText = foreignNameText.substring(1, foreignNameText.length() - 1);

        return foreignNameText;
    }

    /*
     * Returns the character's biography
     */
    public String parseBiography() {
        Element foreignName = doc.selectFirst(foreignNameSelector);
        Node sibling = foreignName.nextSibling();

        StringBuilder summary = new StringBuilder();

        while (!(sibling instanceof Element && ((Element)sibling).hasClass("normal_header")) ||
                sibling instanceof TextNode) {
            summary.append(sibling.toString());
            sibling = sibling.nextSibling();
        }

        return summary.toString();
    }

    /*
     * Returns the number of members that have marked the character
     * as one of their favorites
     */
    public int parseMemberFavorites() {
        Element memberFavorites = doc.selectFirst(memberFavoritesSelector);

        String favoritesText = memberFavorites.ownText();
        favoritesText = StringUtils.removeStart(favoritesText, "Member Favorites: ");
        favoritesText = favoritesText.replace(",", "");
        favoritesText = favoritesText.replace(" ", "");

        return Integer.parseInt(favoritesText);
    }

    /*
     * Returns a map where each key (id of anime that the haracter
     * appeared in) maps to the character's role (Main/Supporting)
     */
    public HashMap<Integer, String> parseAnimeography() {
        return parseAnimeographyOrMangaography(animeographySelector, animeIdPattern);
    }

    /*
     * Returns a map where each key (id of manga that the character
     * appeared in) maps to the character's role (Main/Supporting)
     */
    public HashMap<Integer, String> parseMangaography() {
        return parseAnimeographyOrMangaography(mangaographySelector, mangaIdPattern);
    }

    /*
     * Returns a map where each key (id of person that voice
     * acted the character) maps to the language they voiced the
     * character in
     */
    public HashMap<Integer, String> parseVoiceActors() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        try {
            Element outerTable = doc.selectFirst(voiceActorSelector);
            Element table = outerTable.nextElementSibling();

            while (table.tag().toString().equals("table")) {
                Element tableBody = table.child(0);
                Element tableRow = tableBody.child(0);

                // Parse the content ID
                Element aHref = tableRow.selectFirst("a");
                String url = aHref.attr("href");

                Matcher urlMatcher = personIdPattern.matcher(url);
                urlMatcher.find();

                int id = Integer.parseInt(urlMatcher.group(1));

                // Parse the subtext
                Element small = tableRow.selectFirst("small");
                String subtext = small.ownText();

                map.put(id, subtext);
                table = table.nextElementSibling();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return map;
    }

    /*
     * Takes a selector for an animeography or mangeography table and a pattern
     * to search for anime or manga paths (e.g. anime/31240). Returns a map where
     * each key (id of anime/manga that the character appeared in) maps to the
     * character's role
     */
    private HashMap<Integer, String> parseAnimeographyOrMangaography(String selector, Pattern pattern) {
        HashMap<Integer, String> map = new HashMap<>();

        try {
            Element outerTable = doc.selectFirst(selector);

            Element table = outerTable.nextElementSibling();
            Element tableBody = table.child(0);

            for (Element tableRow : tableBody.children()) {
                // Parse the content ID
                Element aHref = tableRow.selectFirst("a");
                String url = aHref.attr("href");

                Matcher urlMatcher = pattern.matcher(url);
                urlMatcher.find();

                int id = Integer.parseInt(urlMatcher.group(1));

                // Parse the subtext
                Element small = tableRow.selectFirst("small");
                String subtext = small.ownText();

                map.put(id, subtext);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return map;
    }
}
