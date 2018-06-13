package scrapers;

import hibernateUtils.HibernateUtils;
import hibernateUtils.hibernateConverters.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Application{
    public static int getId(String key) {
        String[] keySplit = key.split("-");
        int id = Integer.parseInt(keySplit[keySplit.length - 1]);
        return id;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("src/main/context.xml");
        Converter animeConverter = context.getBean(AnimeConverter.class);
        Converter animeStatisticConverter = context.getBean(AnimeStatisticConverter.class);
        HibernateUtils hibernateUtils = context.getBean(HibernateUtils.class);

        animeConverter.convert("d/anime-31240");
        animeStatisticConverter.convert("d/anime-stats-31240");

        /*
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext ctx = app.run(args);
        ctx.close();
        */
    }

    public void run(String[] args) throws IOException {
    }
}
