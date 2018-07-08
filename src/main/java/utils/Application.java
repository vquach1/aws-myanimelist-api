package utils;

import hibernateUtils.hibernateConverters.animeConverters.AnimeConverter;
import hibernateUtils.hibernateConverters.characterConverters.CharacterConverter;
import hibernateUtils.hibernateConverters.mangaConverters.MangaConverter;
import hibernateUtils.hibernateConverters.mangaConverters.MangaStatisticConverter;
import hibernateUtils.hibernateMappings.lookupTableMappings.GenreType;
import org.springframework.core.task.TaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
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
        Downloader downloader = context.getBean(Downloader.class);
        TaskExecutor taskExecutor = context.getBean(TaskExecutor.class);

        HashMap<Integer, String> animeToIdPathMap =
                (HashMap<Integer, String>)context.getBean("animeIdToPathMap");
        List<Runnable> runnables = new ArrayList<>();

        for (Integer id : animeToIdPathMap.keySet()) {
            runnables.add(() -> {
                try {
                    animeConverter.convert(id);
                } catch (Exception e) {
                    System.out.println("Error while attempting to process " + animeToIdPathMap.get(id));
                    e.printStackTrace();
                }
            });
        }

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
