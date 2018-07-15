package beanConfigs;

import scrapers.*;
import scrapers.animes.AnimeCharacterStaffScraper;
import scrapers.animes.AnimeScraper;
import scrapers.animes.AnimeStatisticScraper;
import scrapers.characters.CharacterScraper;
import scrapers.mangas.MangaScraper;
import scrapers.mangas.MangaStatisticScraper;
import scrapers.persons.PersonScraper;
import hibernateUtils.mappings.lookupTables.GenreType;
import hibernateUtils.mappings.lookupTables.RelatedType;
import utils.Downloader;
import hibernateUtils.daos.GenericDao;
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
import java.util.Map;

@Configuration
public class ConverterConfig {
    @Autowired
    private S3Utils s3Utils;

    @Autowired
    private GenericDao genericDao;

    @Autowired
    private Downloader downloader;

    //region Converters

    @Bean
    public AnimeScraper animeConverter() {
        return new AnimeScraper();
    }

    @Bean
    public AnimeStatisticScraper animeStatisticConverter() {
        return new AnimeStatisticScraper();
    }

    @Bean
    public AnimeCharacterStaffScraper animeCharacterStaffConverter() {
        return new AnimeCharacterStaffScraper();
    }

    @Bean
    public MangaScraper mangaConverter() {
        return new MangaScraper();
    }

    @Bean
    public MangaStatisticScraper mangaStatisticConverter() {
        return new MangaStatisticScraper();
    }

    @Bean
    public CharacterScraper characterConverter() {
        return new CharacterScraper();
    }

    @Bean
    public PersonScraper personCoverter() {
        return new PersonScraper();
    }

    @Bean
    public ProducerAndMagazineScraper producerConverter() {
        return new ProducerAndMagazineScraper();
    }

    //endregion

    @Bean
    public Map<String, GenreType> genreTypeMap() {
        List<GenreType> genreTypes = (List<GenreType>) genericDao.getTableRows(GenreType.class);
        Map<String, GenreType> map = new HashMap<>();
        for (GenreType type : genreTypes) {
            map.put(type.getName(), type);
        }
        return map;
    }

    @Bean
    public HashMap<String, RelatedType> relatedTypeMap() {
        List<RelatedType> genreTypes = (List<RelatedType>) genericDao.getTableRows(RelatedType.class);
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
