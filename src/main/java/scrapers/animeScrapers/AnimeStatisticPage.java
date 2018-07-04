package scrapers.animeScrapers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import scrapers.abstractScrapers.AnimeAndMangaStatisticPage;

public class AnimeStatisticPage extends AnimeAndMangaStatisticPage {
    private static final String watchingSelector = "span[class='dark_text']:contains(Watching:)";
    private static final String planToWatchSelector = "span[class='dark_text']:contains(Plan To Watch:)";

    public AnimeStatisticPage(Document doc) {
        super(doc);
    }

    public int parseWatching() {
        Element watching = parent(watchingSelector);
        return watching == null ? 0 : parseNumber(watching.ownText());
    }

    public int parsePlanToWatch() {
        Element planToWatch = parent(planToWatchSelector);
        return planToWatch == null ? 0 : parseNumber(planToWatch.ownText());
    }
}