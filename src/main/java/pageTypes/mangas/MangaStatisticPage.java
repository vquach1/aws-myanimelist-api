package pageTypes.mangas;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pageTypes.abstracts.AnimeAndMangaStatisticPage;

/*
 * Provides HTML scraping utilities for pages of the path '/manga/{mangaId}/{mangaName}/stats'
 * Example: /manga/21/Death_Note/stats
 */
public class MangaStatisticPage extends AnimeAndMangaStatisticPage {
    private static final String readingSelector = "span[class='dark_text']:contains(Reading:)";
    private static final String planToReadSelector = "span[class='dark_text']:contains(Plan To Read:)";

    public MangaStatisticPage(Document doc) {
        super(doc);
    }

    /*
     * Returns the number of people that are reading the manga.
     * Pulls from 'Reading' row of the 'Summary Stats' section
     */
    public int parseReading() {
        Element reading = parent(readingSelector);
        return reading == null ? 0 : parseNumber(reading.ownText());
    }

    /*
     * Returns the number of people that plan to read the manga.
     * Pulls from 'Plan To Read' row of the 'Summary Stats' section
     */
    public int parsePlanToRead() {
        Element planToRead = parent(planToReadSelector);
        return planToRead == null ? 0 : parseNumber(planToRead.ownText());
    }
}
