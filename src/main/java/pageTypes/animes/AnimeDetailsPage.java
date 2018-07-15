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
        return premiered == null ? "Unknown" : ownText(premiered);
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
}
