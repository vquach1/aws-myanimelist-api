package scrapers;

import awsUtils.S3Utils;
import config.ConverterConfig;
import hibernateUtils.HibernateUtils;
import hibernateUtils.hibernateConverters.*;
import hibernateUtils.hibernateObjects.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application{
    public static int getId(String key) {
        String[] keySplit = key.split("-");
        int id = Integer.parseInt(keySplit[keySplit.length - 1]);
        return id;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
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
