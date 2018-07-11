package pageTypes.animes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pageTypes.abstracts.AnimeAndMangaDetailsPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Provides HTML scraping utilities for pages of the path 'anime/{animeId}/{animeTitle}'
 * Example: anime/1735/Naruto__Shippuuden
 */
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

    /* Returns the number of episodes from the 'Episodes' row
     * in the 'Information' section
     */
    public String parseEpisodes() {
        Element episode = parent(episodeSelector);
        return ownText(episode);
    }

    /*
     * Returns the airing start and end dates from the 'Aired' row
     * in the 'Information' section
     */
    public String parseAired() {
        Element aired = parent(airedSelector);
        return ownText(aired);
    }

    /*
     * Returns the airing season from the 'Premiered' row
     * in the 'Information' section
     */
    public String parsePremiered() {
        Element premiered = nextElementSibling(premieredSelector);
        return ownText(premiered);
    }

    /*
     * Returns the broadcast day/time from the 'Broadcast' row
     * in the 'Information' section
     */
    public String parseBroadcast() {
        Element broadcast = parent(broadcastSelector);
        return ownText(broadcast);
    }

    /*
     * Returns the media source from the 'Source' row
     * in the 'Information' section
     */
    public String parseSource() {
        Element source = parent(sourceSelector);
        return ownText(source);
    }

    /*
     * Returns the episode duration from the 'Duration' row
     * in the 'Information' section
     */
    public String parseDuration() {
        Element duration = parent(durationSelector);
        return ownText(duration);
    }

    /*
     * Returns the age rating from the 'Rating' row
     * in the 'Information' section
     */
    public String parseRating() {
        Element rating = parent(ratingSelector);
        return ownText(rating);
    }

    /*
     * Returns a list of producer ids from the 'Producers' row
     * in the 'Information' section
     */
    public List<Integer> parseProducers() {
        return parseIds(producerSelector, producerIdPattern);
    }

    /*
     * Returns a list of producer ids from the 'Licensors' row
     * in the 'Information' section
     */
    public List<Integer> parseLicensors() {
        return parseIds(licensorSelector, producerIdPattern);
    }

    /*
     * Returns a list of producer ids from the 'Studios' row
     * in the 'Information' section
     */
    public List<Integer> parseStudios() {
        return parseIds(studioSelector, producerIdPattern);
    }

    /*
     * Returns a list of manga paths from the 'Adaptation' row
     * in the 'Related Anime' section
     */
    public List<String> parseMangaAdaptations() {
        return parseRelated(adaptationSelector);
    }

    /*
     * Returns a list of anime paths from the 'Prequel' row
     * in the 'Related Anime' section
     */
    public List<String> parsePrequels() {
        return parseRelated(prequelSelector);
    }

    /*
     * Returns a list of anime paths from the 'Sequel' row
     * in the 'Related Anime' section
     */
    public List<String> parseSequels() {
        return parseRelated(sequelSelector);
    }

    /*
     * Returns a list of anime paths from the 'Spin-off' row
     * in the 'Related Anime' section
     */
    public List<String> parseSpinoffs() {
        return parseRelated(spinoffSelector);
    }

    /*
     * Returns a list of anime paths from the 'Side story' row
     * in the 'Related Anime' section
     */
    public List<String> parseSideStories() {
        return parseRelated(sideStorySelector);
    }

    /*
     * Returns a list of anime paths from the 'Alternative setting' row
     * in the 'Related Anime' section
     */
    public List<String> parseAlternativeSettings() {
        return parseRelated(altSettingSelector);
    }

    /*
     * Returns a list of anime paths from the 'Alternative version' row
     * in the 'Related Anime' section
     */
    public List<String> parseAlternativeVersions() {
        return parseRelated(altVersionSelector);
    }

    /*
     * Returns a list of anime paths from the 'Summary' row
     * in the 'Related Anime' section
     */
    public List<String> parseSummaries() {
        return parseRelated(summarySelector);
    }

    /*
     * Returns a list of anime paths from the 'Parent story' row
     * in the 'Related Anime' section
     */
    public List<String> parseParentStories() {
        return parseRelated(parentStorySelector);
    }

    /*
     * Returns a list of anime paths from the 'Other' row
     * in the 'Related Anime' section
     */
    public List<String> parseOthers() {
        return parseRelated(otherSelector);
    }

    /*
     * Returns a list of anime or manga paths from the selected
     * row in the 'Related Anime' section
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
