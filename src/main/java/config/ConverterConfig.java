package config;

import awsUtils.S3Utils;
import hibernateUtils.hibernateConverters.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {
    @Bean
    public CharacterConverter characterConverter() {
        return new CharacterConverter(S3Utils.MAL_SCRAPE_CHARACTERS);
    }

    @Bean
    public PersonConverter personConverter() {
        return new PersonConverter(S3Utils.MAL_SCRAPE_PERSONS);
    }

    @Bean
    public AnimeConverter animeConverter() {
        return new AnimeConverter(S3Utils.MAL_SCRAPE_ANIME);
    }

    @Bean
    public ProducerConverter producerConverter() {
        return new ProducerConverter(S3Utils.MAL_SCRAPE_PRODUCERS);
    }

    @Bean
    public AnimeStatisticConverter animeStatisticConverter() {
        return new AnimeStatisticConverter(S3Utils.MAL_SCRAPE_ANIME_STATISTICS);
    }
}
