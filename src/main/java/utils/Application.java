package utils;

import hibernateUtils.hibernateConverters.mangaConverters.MangaConverter;
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
        HashMap<Integer, String> mangaToIdPathMap = (HashMap<Integer, String>)context.getBean("mangaIdToPathMap");


        System.out.println(s3Utils.objectMissingOrOutdated("manga/100/Prism_Palette"));
        System.out.println(s3Utils.objectMissingOrOutdated("foo"));

        /*
        ProducerAndMagazineConverter producerAndMagazineConverter =
                context.getBean(ProducerAndMagazineConverter.class);
        producerAndMagazineConverter.convert();
        */

        MangaConverter mangaConverter = context.getBean(MangaConverter.class);
        int counter = 1;
        for (Integer id : mangaToIdPathMap.keySet()) {
            System.out.println("Counter: " + counter + " Id: " + id);
            mangaConverter.convert(id);
            counter++;
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
