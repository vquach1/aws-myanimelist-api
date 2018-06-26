package scrapers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrapers.abstractScrapers.MalPage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProducerAndMagazinePage extends MalPage {
    private static final String companySelector = "a[class='genre-name-link']";

    public ProducerAndMagazinePage(Document doc) { super(doc); }

    /*
     * Returns a map where each entry maps a key (id of producer)
     * maps to the producer's name
     */
    public HashMap<Integer, String> parseProducers() {
        return parseCompanies(producerIdPattern);
    }

    /*
     * Returns a map where each entry maps a key (id of magazine)
     * maps to the magazine's name
     */
    public HashMap<Integer, String> parseMagazines() {
        return parseCompanies(magazineIdPattern);
    }

    /*
     * Returns a map where each entry maps a key (id of producer
     * or magazine company) maps to the company's name. Whether
     * producers or magazines are parsed depends on the provided
     * pattern
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
