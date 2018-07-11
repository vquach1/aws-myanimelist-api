package scrapers.mangas;

import scrapers.abstracts.AnimeAndMangaStatisticScraper;
import hibernateUtils.mappings.mangas.MangaStatistic;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.mangas.MangaStatisticPage;

import java.util.HashMap;

public class MangaStatisticScraper extends AnimeAndMangaStatisticScraper {
    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    public MangaStatisticScraper() {}

    public MangaStatistic convert(int mangaId) {
        String path = mangaIdToPathMap.get(mangaId) + "/stats";
        Document doc = parseHtml(path);
        MangaStatisticPage page = new MangaStatisticPage(doc);

        if (page.isEmptyPage()) {
            return null;
        }

        MangaStatistic mangaStat = new MangaStatistic();

        // Set statistics shared by anime and manga statistics
        convertCommonStats(mangaStat, page);

        // Set id and statistics unique to a manga page
        mangaStat.setId(mangaId);
        mangaStat.setReading(page.parseReading());
        mangaStat.setPlanToRead(page.parsePlanToRead());

        genericDao.saveOrUpdateMalMapping(mangaStat);
        return mangaStat;
    }
}
