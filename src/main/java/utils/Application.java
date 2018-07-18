package utils;

import scrapers.animes.AnimeCharacterStaffScraper;
import scrapers.animes.AnimeScraper;
import scrapers.animes.AnimeStatisticScraper;
import scrapers.characters.CharacterScraper;
import scrapers.mangas.MangaScraper;
import scrapers.mangas.MangaStatisticScraper;
import hibernateUtils.mappings.lookupTables.GenreType;
import org.springframework.core.task.TaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import unused.Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Application{
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        S3Utils s3Utils = context.getBean(S3Utils.class);
        HashMap<Integer, GenreType> genreMap = (HashMap<Integer, GenreType>)context.getBean("genreTypeMap");
        MangaScraper mangaScraper = context.getBean(MangaScraper.class);
        AnimeStatisticScraper animeStatisticScraper = context.getBean(AnimeStatisticScraper.class);
        MangaStatisticScraper mangaStatisticScraper = context.getBean(MangaStatisticScraper.class);
        AnimeScraper animeScraper = context.getBean(AnimeScraper.class);
        AnimeCharacterStaffScraper animeCharacterStaffScraper = context.getBean(AnimeCharacterStaffScraper.class);
        CharacterScraper characterScraper = context.getBean(CharacterScraper.class);
        Downloader downloader = context.getBean(Downloader.class);
        TaskExecutor taskExecutor = context.getBean(TaskExecutor.class);

        HashMap<Integer, String> animeToIdPathMap =
                (HashMap<Integer, String>)context.getBean("animeIdToPathMap");
        HashMap<Integer, String> mangaToIdPathMap =
                (HashMap<Integer, String>)context.getBean("mangaIdToPathMap");

        List<Runnable> runnables = new ArrayList<>();

        /*
        int count = 0;
        int beginCount = 0;
        for (Integer id : animeToIdPathMap.keySet()) {
            count++;
            if (count < beginCount) {
                continue;
            }

            Integer countInt = new Integer(count);
            runnables.add(() -> {
                try {
                    System.out.print(countInt + " ");
                    animeStatisticScraper.convert(id);
                } catch (Exception e) {
                    System.out.println("Error while attempting to process " + mangaToIdPathMap.get(id));
                    e.printStackTrace();
                }
            });
        }
        */

        for (Runnable r : runnables) {
            taskExecutor.execute(r);
        }

        /*
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext ctx = app.run(args);
        ctx.close();
        */
    }

    public void run(String[] args) throws IOException {
    }
}
