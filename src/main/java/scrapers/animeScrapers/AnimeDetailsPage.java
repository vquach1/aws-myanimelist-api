package scrapers.animeScrapers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import scrapers.abstractScrapers.AnimeAndMangaDetailsPage;
import scrapers.abstractScrapers.MalPage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AnimeDetailsPage extends AnimeAndMangaDetailsPage {
    private static final String episodeSelector = "span[class='dark_text']:contains(Episodes:)";
    private static final String airedSelector = "span[class='dark_text']:contains(Aired:)";
    private static final String premieredSelector = "span[class='dark_text']:contains(Premiered:)";
    private static final String broadcastSelector = "span[class='dark_text']:contains(Broadcast:)";
    private static final String producerSelector = "span[class='dark_text']:contains(Producers:)";
    private static final String licensorSelector = "span[class='dark_text']:contains(Licensors:)";
    private static final String studioSelector = "span[class='dark_text']:contains(Studios:)";
    private static final String sourceSelector = "span[class='dark_text']:contains(Source:)";
    private static final String durationSelector = "span[class='dark_text']:contains(Duration:)";
    private static final String ratingSelector = "span[class='dark_text']:contains(Rating:)";
    private static final String spinoffSelector = "td[class='ar fw-n borderClass']:contains(Spin-off:)";

    public AnimeDetailsPage(Document doc) {
        super(doc);
    }

    public String parseEpisodes() {
        Element episode = parent(episodeSelector);
        return ownText(episode);
    }

    public String parseAired() {
        Element aired = parent(airedSelector);
        return ownText(aired);
    }

    public String parsePremiered() {
        Element premiered = nextElementSibling(premieredSelector);
        return ownText(premiered);
    }

    public String parseBroadcast() {
        Element broadcast = parent(broadcastSelector);
        return ownText(broadcast);
    }

    public String parseSource() {
        Element source = parent(sourceSelector);
        return ownText(source);
    }

    public String parseDuration() {
        Element duration = parent(durationSelector);
        return ownText(duration);
    }

    public String parseRating() {
        Element rating = parent(ratingSelector);
        return ownText(rating);
    }

    public List<Integer> parseProducers() {
        return parseIds(producerSelector, producerIdPattern);
    }

    public List<Integer> parseLicensors() {
        return parseIds(licensorSelector, producerIdPattern);
    }

    public List<Integer> parseStudios() {
        return parseIds(studioSelector, producerIdPattern);
    }

    public List<String> parseSpinoffs() {
        Element spinoffs = nextElementSibling(spinoffSelector);
        return spinoffs.children().stream().map(elem -> elem.attr("href")).collect(Collectors.toList());
    }
}