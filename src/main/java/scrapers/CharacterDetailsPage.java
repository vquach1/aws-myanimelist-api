package scrapers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
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

    public String parseName() {
        Element name = doc.selectFirst(nameSelector);
        return ownText(name);
    }

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

    public int parseMemberFavorites() {
        Element memberFavorites = doc.selectFirst(memberFavoritesSelector);

        String favoritesText = memberFavorites.ownText();
        favoritesText = StringUtils.removeStart(favoritesText, "Member Favorites: ");
        favoritesText = favoritesText.replace(",", "");
        favoritesText = favoritesText.replace(" ", "");

        return Integer.parseInt(favoritesText);
    }

    /* Maps anime ID -> character role */
    public HashMap<Integer, String> parseAnimeography() {
        return parseAnimeMangaTables(animeographySelector, animeIdPattern);
    }

    /* Maps manga ID -> character role */
    public HashMap<Integer, String> parseMangaography() {
        return parseAnimeMangaTables(mangaographySelector, mangaIdPattern);
    }

    /* Maps person ID -> language */
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

    private  HashMap<Integer, String> parseAnimeMangaTables(String selector, Pattern pattern) {
        HashMap<Integer, String> map = new HashMap<Integer, String>();

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
