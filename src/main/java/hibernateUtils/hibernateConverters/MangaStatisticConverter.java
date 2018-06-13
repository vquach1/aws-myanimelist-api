package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.MangaStatistic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrapers.MangaStatisticPage;

public class MangaStatisticConverter extends AnimeMangaStatisticConverter {
    public MangaStatisticConverter(String bucketName) {
        super(bucketName);
    }

    public void convert(String key) {
        String[] keySplit = key.split("-");
        int id = Integer.parseInt(keySplit[keySplit.length - 1]);

        String rawHtml = s3Utils.readObject(bucketName, key);
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        MangaStatisticPage page = new MangaStatisticPage(doc);
        MangaStatistic mangaStat = new MangaStatistic();

        // Set statistics shared by anime and manga statistics
        convertCommonStats(mangaStat, page);

        // Set id and statistics unique to a manga page
        mangaStat.setId(id);
        mangaStat.setReading(page.parseReading());
        mangaStat.setPlanToRead(page.parsePlanToRead());

        hibernateUtils.updateMalMapping(id, mangaStat);
    }
}
