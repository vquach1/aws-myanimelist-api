package hibernateUtils.hibernateConverters.animeConverters;

import hibernateUtils.hibernateConverters.abstractConverters.AnimeAndMangaStatisticConverter;
import hibernateUtils.hibernateMappings.animeMappings.AnimeStatistic;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scrapers.animeScrapers.AnimeStatisticPage;

import java.util.HashMap;

public class AnimeStatisticConverter extends AnimeAndMangaStatisticConverter {
    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    public AnimeStatisticConverter() {}

    public void convert(int animeId) {
        String path = animeIdToPathMap.get(animeId) + "/stats";
        Document doc = parseHtml(path);
        AnimeStatisticPage page = new AnimeStatisticPage(doc);
        AnimeStatistic animeStat = new AnimeStatistic();

        // Set statistics shared by anime and manga statistics
        convertCommonStats(animeStat, page);

        // Set id and statistics unique to an anime page
        animeStat.setId(animeId);
        animeStat.setWatching(page.parseWatching());
        animeStat.setPlanToWatch(page.parsePlanToWatch());

        hibernateUtils.updateMalMapping(animeId, animeStat);
    }
}
