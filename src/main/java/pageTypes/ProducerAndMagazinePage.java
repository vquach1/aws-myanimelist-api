package pageTypes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pageTypes.abstracts.MalPage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Provides HTML scraping utilities for pages of either path:
 * -'/anime/producer'
 * -'/manga/magazine'
 */
public class ProducerAndMagazinePage extends MalPage {
    private static final String companySelector = "a[class='genre-name-link']";

    public ProducerAndMagazinePage(Document doc) { super(doc); }

    /*
     * Returns a map pulling data from path 'anime/producer' where...
     * -Key: Id of producer
     * -Value: Producer's name
     */
    public HashMap<Integer, String> parseProducers() {
        return parseCompanies(producerIdPattern);
    }

    /*
     * Returns a map pulling data from path 'manga/magazine' where...
     * -Key: Id of magazine
     * -Value: Magazine's name
     */
    public HashMap<Integer, String> parseMagazines() {
        return parseCompanies(magazineIdPattern);
    }

    /*
     * Returns a map where...
     * -Key: Id of producer/magazine
     * -Name: Producer/magazine's name
     *
     * The type of values looked searched for depends on
     * the provided Pattern
     */
    public HashMap<Integer, String> parseCompanies(Pattern pattern) {
        HashMap<Integer, String> map = new HashMap<>();
        Elements companies = doc.select(companySelector);

        for (Element companyElem : companies) {
            String href = companyElem.attr("href");
            String text = companyElem.text();

            Matcher companyIdMatcher = pattern.matcher(href);
            companyIdMatcher.find();

            int companyId = Integer.parseInt(companyIdMatcher.group(1));
            String companyName = text.replaceAll(" \\(\\d+\\)$", "");

            map.put(companyId, companyName);
        }

        return map;
    }
}
