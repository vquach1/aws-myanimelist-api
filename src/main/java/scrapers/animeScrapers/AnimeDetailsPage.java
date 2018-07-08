package scrapers.animeScrapers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import scrapers.abstractScrapers.AnimeAndMangaDetailsPage;

import java.util.ArrayList;
import java.util.List;
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
    private static final String adaptationSelector = "td[class='ar fw-n borderClass']:contains(Adaptation:)";
    private static final String prequelSelector = "td[class='ar fw-n borderClass']:contains(Prequel:)";
    private static final String sequelSelector = "td[class='ar fw-n borderClass']:contains(Sequel:)";
    private static final String otherSelector = "td[class='ar fw-n borderClass']:contains(Other:)";
    private static final String sideStorySelector = "td[class='ar fw-n borderClass']:contains(Side story:)";
    private static final String altSettingSelector = "td[class='ar fw-n borderClass']:contains(Alternative setting:)";
    private static final String altVersionSelector = "td[class='ar fw-n borderClass']:contains(Alternative version:)";
    private static final String summarySelector = "td[class='ar fw-n borderClass']:contains(Summary:)";
    private static final String parentStorySelector = "td[class='ar fw-n borderClass']:contains(Parent story:)";

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

    public List<String> parseMangaAdaptations() {
        return parseRelated(adaptationSelector);
    }

    public List<String> parsePrequels() {
        return parseRelated(prequelSelector);
    }

    public List<String> parseSequels() {
        return parseRelated(sequelSelector);
    }

    public List<String> parseSpinoffs() {
        return parseRelated(spinoffSelector);
    }

    public List<String> parseSideStories() {
        return parseRelated(sideStorySelector);
    }

    public List<String> parseAlternativeSettings() {
        return parseRelated(altSettingSelector);
    }

    public List<String> parseAlternativeVersions() {
        return parseRelated(altVersionSelector);
    }

    public List<String> parseSummaries() {
        return parseRelated(summarySelector);
    }

    public List<String> parseParentStories() {
        return parseRelated(parentStorySelector);
    }

    public List<String> parseOthers() {
        return parseRelated(otherSelector);
    }

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
