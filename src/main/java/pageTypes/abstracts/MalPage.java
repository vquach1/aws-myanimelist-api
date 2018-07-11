package pageTypes.abstracts;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

/*
 * Encapsulates the set of utilities to parse information from
 * a MyAnimeList page. Each type of path is given a concrete object
 * type.
 */
abstract public class MalPage{
    protected Document doc;

    private static String producerIdRegex = "/anime/producer/(\\d+)";
    private static String magazineIdRegex = "/manga/magazine/(\\d+)";
    private static String animeIdRegex = "/anime/(\\d+)";
    private static String mangaIdRegex = "/manga/(\\d+)";
    private static String personIdRegex = "/people/(\\d+)";
    private static String genreIdRegex = "/genre/(\\d+)";

    protected static Pattern producerIdPattern = Pattern.compile(producerIdRegex);
    protected static Pattern magazineIdPattern = Pattern.compile(magazineIdRegex);
    protected static Pattern animeIdPattern = Pattern.compile(animeIdRegex);
    protected static Pattern mangaIdPattern = Pattern.compile(mangaIdRegex);
    protected static Pattern personIdPattern = Pattern.compile(personIdRegex);
    protected static Pattern genreIdPattern = Pattern.compile(genreIdRegex);

    protected String invalidIdSelector = "div[class='badresult']:contains(Invalid ID provided.)";

    public MalPage(Document doc) {
        this.doc = doc;
    }

    /*
     * Returns the Element's parent() if the Element is not null.
     * Otherwise, returns an empty string to avoid a null exception
     */
    public Element parent(String selector) {
        Element match = doc.selectFirst(selector);
        return match == null ? null : match.parent();
    }

    /*
     * Returns the Element's nextElementSibling() if the Element is not null.
     * Otherwise, returns an empty string to avoid a null exception
     */
    public Element nextElementSibling(String selector) {
        Element match = doc.selectFirst(selector);
        return match == null ? null : match.nextElementSibling();
    }

    /*
     * Returns the Element's ownText() if the Element is not null.
     * Otherwise, returns an empty string to avoid a null exception
     */
    public String ownText(Element elem) {
        return elem == null ? "" : elem.ownText();
    }

    /*
     * Returns the Element's html() if the Element is not null.
     * Otherwise, returns an empty string to avoid a null exception
     */
    public String html(Element elem) {
        return elem == null ? "" : elem.html();
    }

    /*
     * Converts a string representation of a number into its
     * integer form, while respecting commas
     */
    public int parseNumber(String numberStr) {
        return Integer.parseInt(numberStr.replace(",", ""));
    }

    /*
     * Returns true if the page contains no content or is labelled
     * with a 404 status code. This will return true in situations
     * where the sitemap points to an invalid path
     */
    public boolean isEmptyPage() {
        return !doc.hasText() || doc.selectFirst(invalidIdSelector) != null;
    }
}
