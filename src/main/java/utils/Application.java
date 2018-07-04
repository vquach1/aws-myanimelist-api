package utils;

import hibernateUtils.hibernateConverters.animeConverters.AnimeConverter;
import hibernateUtils.hibernateConverters.characterConverters.CharacterConverter;
import hibernateUtils.hibernateConverters.mangaConverters.MangaConverter;
import hibernateUtils.hibernateConverters.mangaConverters.MangaStatisticConverter;
import hibernateUtils.hibernateMappings.GenreType;
import hibernateUtils.hibernateMappings.animeMappings.Anime;
import hibernateUtils.hibernateMappings.mangaMappings.MangaStatistic;
import utils.S3Utils;
import hibernateUtils.hibernateConverters.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Application{
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        S3Utils s3Utils = context.getBean(S3Utils.class);
        HashMap<Integer, GenreType> genreMap = (HashMap<Integer, GenreType>)context.getBean("genreTypeMap");
        MangaConverter mangaConverter = context.getBean(MangaConverter.class);
        MangaStatisticConverter mangaStatisticConverter = context.getBean(MangaStatisticConverter.class);
        AnimeConverter animeConverter = context.getBean(AnimeConverter.class);
        CharacterConverter characterConverter = context.getBean(CharacterConverter.class);

        HashMap<Integer, String> characterToIdPathMap =
                (HashMap<Integer, String>)context.getBean("characterIdToPathMap");
        for (Integer id : characterToIdPathMap.keySet()) {
            try {
               characterConverter.convert(id);
            } catch (Exception e) {
                System.out.println("Error while attempting to process " + characterToIdPathMap.get(id));
                e.printStackTrace();
            }
        }

        /*
        List<String> keys = new ArrayList<>();
        //keys.addAll(s3Utils.getKeys("mal-scrape", "manga/stats/"));
        for (String oldKey : keys) {
            String rawHtml = s3Utils.readObject("mal-scrape", oldKey);
            Document doc = Jsoup.parse(rawHtml);
            Element elem = doc.selectFirst("meta[property='og:url']");

            if (elem != null) {
                String newKey = StringUtils.removeStart(elem.attr("content"), "https://myanimelist.net/");
                System.out.println(newKey);
                s3Utils.moveObject(s3Utils.BUCKET, oldKey, s3Utils.BUCKET, newKey);
            }
        }
        */

        /*
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext ctx = app.run(args);
        ctx.close();
        */
    }

    public void run(String[] args) throws IOException {
    }
}
