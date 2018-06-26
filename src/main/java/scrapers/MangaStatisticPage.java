package scrapers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import scrapers.abstractScrapers.AnimeAndMangaStatisticPage;

public class MangaStatisticPage extends AnimeAndMangaStatisticPage {
    private static final String readingSelector = "span[class='dark_text']:contains(Reading:)";
    private static final String planToReadSelector = "span[class='dark_text']:contains(Plan To Read:)";

    public MangaStatisticPage(Document doc) {
        super(doc);
    }

    public int parseReading() {
        Element reading = parent(readingSelector);
        return reading == null ? 0 : parseNumber(reading.ownText());
    }

    public int parsePlanToRead() {
        Element planToRead = parent(planToReadSelector);
        return planToRead == null ? 0 : parseNumber(planToRead.ownText());
    }
}
