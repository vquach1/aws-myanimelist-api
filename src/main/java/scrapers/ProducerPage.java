package scrapers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProducerPage extends MalPage {
    private static final String producerSelector = "a[class='genre-name-link']";

    public ProducerPage(Document doc) {
        super(doc);
    }

    public HashMap<Integer, String> parseProducers() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        Elements producers = doc.select(producerSelector);

        for (Element producerElem : producers) {
            String href = producerElem.attr("href");
            String text = producerElem.text();

            Matcher producerIdMatcher = producerIdPattern.matcher(href);
            producerIdMatcher.find();

            int id = Integer.parseInt(producerIdMatcher.group(1));
            String producer = text.replaceAll(" \\(\\d+\\)$", "");

            map.put(id, producer);
        }

        return map;
    }
}
