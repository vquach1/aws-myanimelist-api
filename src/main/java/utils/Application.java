package utils;

import scrapers.animes.AnimeCharacterStaffScraper;
import scrapers.animes.AnimeScraper;
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

        int count = 0;
        int beginCount = 0;
        for (Integer id : mangaToIdPathMap.keySet()) {
            count++;
            if (count < beginCount) {
                continue;
            }

            Integer countInt = new Integer(count);
            runnables.add(() -> {
                try {
                    System.out.print(countInt + " ");
                    mangaScraper.convert(id);
                } catch (Exception e) {
                    System.out.println("Error while attempting to process " + mangaToIdPathMap.get(id));
                    e.printStackTrace();
                }
            });
        }

        //Runnable task = () -> animeCharacterStaffConverter.convert(20);
        //dependencyGraph.insert(animeToIdPathMap.get(20) + "/characters", task);
        //runnables.add(task);

        for (Runnable r : runnables) {
            taskExecutor.execute(r);
        }

        /*
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
        */

        /*
        HashMap<Integer, String> mangaIdToPathMap =
                (HashMap<Integer, String>)context.getBean("mangaIdToPathMap");

        for (Integer id : mangaIdToPathMap.keySet()) {
            String newKey = mangaIdToPathMap.get(id) + "/pics";
            if (s3Utils.objectMissingOrOutdated(newKey)) {
                downloader.download(newKey);
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
