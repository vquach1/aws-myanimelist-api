package pageTypes.animes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pageTypes.abstracts.AnimeAndMangaStatisticPage;

/*
 * Provides HTML scraping utilities for pages of the path 'anime/{animeId}/{animeTitle}/stats'
 * Example: anime/1735/Naruto__Shippuuden
 */
public class AnimeStatisticPage extends AnimeAndMangaStatisticPage {
    private static final String watchingSelector = "span[class='dark_text']:contains(Watching:)";
    private static final String planToWatchSelector = "span[class='dark_text']:contains(Plan To Watch:)";

    public AnimeStatisticPage(Document doc) {
        super(doc);
    }

    /*
     * Returns the number of people that are watching the anime.
     * Pulls from 'Watching' row of the 'Summary Stats' section
     */
    public int parseWatching() {
        Element watching = parent(watchingSelector);
        return watching == null ? 0 : parseNumber(watching.ownText());
    }

    /*
     * Returns the number of people that plan to watch the anime.
     * Pulls from 'Plan To Watch' row of the 'Summary Stats' section
     */
    public int parsePlanToWatch() {
        Element planToWatch = parent(planToWatchSelector);
        return planToWatch == null ? 0 : parseNumber(planToWatch.ownText());
    }
}
