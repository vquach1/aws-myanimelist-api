package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.AnimeStatistic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrapers.AnimeStatisticPage;

public class AnimeStatisticConverter extends AnimeMangaStatisticConverter {
    public AnimeStatisticConverter(String bucketName) {
        super(bucketName);
    }

    public void convert(String key) {
        String[] keySplit = key.split("-");
        int id = Integer.parseInt(keySplit[keySplit.length - 1]);

        String rawHtml = s3Utils.readObject(bucketName, key);
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        AnimeStatisticPage page = new AnimeStatisticPage(doc);
        AnimeStatistic animeStat = new AnimeStatistic();

        // Set statistics shared by anime and manga statistics
        convertCommonStats(animeStat, page);

        // Set id and statistics unique to an anime page
        animeStat.setId(id);
        animeStat.setWatching(page.parseWatching());
        animeStat.setPlanToWatch(page.parsePlanToWatch());

        hibernateUtils.updateMalMapping(id, animeStat);
    }
}
