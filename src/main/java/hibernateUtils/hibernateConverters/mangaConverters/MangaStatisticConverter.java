package hibernateUtils.hibernateConverters.mangaConverters;

import hibernateUtils.hibernateConverters.abstractConverters.AnimeAndMangaStatisticConverter;
import hibernateUtils.hibernateMappings.mangaMappings.MangaStatistic;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scrapers.mangaScrapers.MangaStatisticPage;

import java.util.HashMap;

public class MangaStatisticConverter extends AnimeAndMangaStatisticConverter {
    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    public MangaStatisticConverter() {}

    public void convert(int mangaId) {
        String path = mangaIdToPathMap.get(mangaId) + "/stats";
        Document doc = parseHtml(path);
        MangaStatisticPage page = new MangaStatisticPage(doc);

        if (page.isEmptyPage()) {
            return;
        }

        MangaStatistic mangaStat = new MangaStatistic();

        // Set statistics shared by anime and manga statistics
        convertCommonStats(mangaStat, page);

        // Set id and statistics unique to a manga page
        mangaStat.setId(mangaId);
        mangaStat.setReading(page.parseReading());
        mangaStat.setPlanToRead(page.parsePlanToRead());

        hibernateUtils.saveOrUpdateMalMapping(mangaStat);
    }
}
