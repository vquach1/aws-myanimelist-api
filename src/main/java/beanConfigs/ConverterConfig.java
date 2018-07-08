package beanConfigs;

import hibernateUtils.hibernateConverters.*;
import hibernateUtils.hibernateConverters.animeConverters.AnimeConverter;
import hibernateUtils.hibernateConverters.animeConverters.AnimeStatisticConverter;
import hibernateUtils.hibernateConverters.characterConverters.CharacterConverter;
import hibernateUtils.hibernateConverters.mangaConverters.MangaConverter;
import hibernateUtils.hibernateConverters.mangaConverters.MangaStatisticConverter;
import hibernateUtils.hibernateConverters.personConverters.PersonConverter;
import hibernateUtils.hibernateMappings.lookupTableMappings.GenreType;
import hibernateUtils.hibernateMappings.lookupTableMappings.RelatedType;
import utils.Downloader;
import utils.HibernateUtils;
import utils.S3Utils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;

@Configuration
public class ConverterConfig {
    @Autowired
    private S3Utils s3Utils;

    @Autowired
    private HibernateUtils hibernateUtils;

    @Autowired
    private Downloader downloader;

    //region Converters

    @Bean
    public AnimeConverter animeConverter() {
        return new AnimeConverter();
    }

    @Bean
    public AnimeStatisticConverter animeStatisticConverter() {
        return new AnimeStatisticConverter();
    }

    @Bean
    public MangaConverter mangaConverter() {
        return new MangaConverter();
    }

    @Bean
    public MangaStatisticConverter mangaStatisticConverter() {
        return new MangaStatisticConverter();
    }

    @Bean
    public CharacterConverter characterConverter() {
        return new CharacterConverter();
    }

    @Bean
    public PersonConverter personCoverter() {
        return new PersonConverter();
    }

    @Bean
    public ProducerAndMagazineConverter producerConverter() {
        return new ProducerAndMagazineConverter();
    }

    //endregion

    @Bean
    public HashMap<Integer, GenreType> genreTypeMap() {
        List<GenreType> genreTypes = (List<GenreType>)hibernateUtils.getTableRows(GenreType.class);
        HashMap<Integer, GenreType> map = new HashMap<>();
        for (GenreType type : genreTypes) {
            map.put(type.getId(), type);
        }
        return map;
    }

    @Bean
    public HashMap<String, RelatedType> relatedTypeMap() {
        List<RelatedType> genreTypes = (List<RelatedType>)hibernateUtils.getTableRows(RelatedType.class);
        HashMap<String, RelatedType> map = new HashMap<>();
        for (RelatedType type : genreTypes) {
            map.put(type.getName(), type);
        }
        return map;
    }

    @Bean
    public HashMap<Integer, String> animeIdToPathMap() {
        HashMap<Integer, String> map = new HashMap<>();
        insertIdPathPairs(map, "sitemap/anime-000.xml");
        return map;
    }

    @Bean
    public HashMap<Integer, String> mangaIdToPathMap() {
        HashMap<Integer, String> map = new HashMap<>();
        insertIdPathPairs(map, "sitemap/manga-000.xml");
        return map;
    }

    @Bean
    public HashMap<Integer, String> characterIdToPathMap() {
        HashMap<Integer, String> map = new HashMap<>();
        insertIdPathPairs(map, "sitemap/character-000.xml");
        insertIdPathPairs(map, "sitemap/character-001.xml");
        return map;
    }

    @Bean
    public HashMap<Integer, String> peopleIdToPathMap() {
        HashMap<Integer, String> map = new HashMap<>();
        insertIdPathPairs(map, "sitemap/people-000.xml");
        return map;
    }

    private void insertIdPathPairs(HashMap<Integer, String> map, String s3IndexPath) {
        if (s3Utils.objectMissingOrOutdated(s3IndexPath)) {
            downloader.download(s3IndexPath);
        }

        String index = s3Utils.readObject(s3IndexPath);
        Document doc = Jsoup.parse(index, "", Parser.xmlParser());

        for (Element elem : doc.select("loc")) {
            String path = StringUtils.removeStart(elem.ownText(), "https://myanimelist.net/");
            int id = Integer.valueOf(path.split("/")[1]);
            map.put(id, path);
        }
    }
}
