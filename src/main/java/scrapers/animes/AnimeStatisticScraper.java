package scrapers.animes;

import scrapers.abstracts.AnimeAndMangaStatisticScraper;
import hibernateUtils.mappings.animes.AnimeStatistic;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.animes.AnimeStatisticPage;

import java.util.HashMap;

public class AnimeStatisticScraper extends AnimeAndMangaStatisticScraper {
    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    public AnimeStatisticScraper() {}

    public AnimeStatistic convert(int animeId) {
        String path = animeIdToPathMap.get(animeId) + "/stats";
        System.out.println("Converting stat " + path);
        Document doc = parseHtml(path);
        AnimeStatisticPage page = new AnimeStatisticPage(doc);
        AnimeStatistic animeStat = new AnimeStatistic();

        if (page.isEmptyPage()) {
            return null;
        }

        // Set statistics shared by anime and manga statistics
        convertCommonStats(animeStat, page);

        // Set id and statistics unique to an anime page
        animeStat.setId(animeId);
        animeStat.setWatching(page.parseWatching());
        animeStat.setPlanToWatch(page.parsePlanToWatch());

        genericDao.saveOrUpdateMalMapping(animeStat);
        return animeStat;
    }
}
