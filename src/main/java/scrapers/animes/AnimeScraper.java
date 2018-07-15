package scrapers.animes;

import com.amazonaws.util.StringUtils;
import hibernateUtils.daos.MangaDao;
import hibernateUtils.mappings.mangas.Manga;
import scrapers.abstracts.Scraper;
import hibernateUtils.daos.AnimeDao;
import hibernateUtils.mappings.lookupTables.GenreType;
import hibernateUtils.mappings.lookupTables.RelatedType;
import hibernateUtils.mappings.animes.*;
import hibernateUtils.mappings.lookupTables.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.animes.AnimeDetailsPage;
import scrapers.mangas.MangaScraper;

import javax.annotation.PostConstruct;
import java.util.*;

public class AnimeScraper extends Scraper {
    private HashMap<String, AnimeType> animeTypeMap = new HashMap<>();
    private HashMap<String, AnimeStatusType> animeStatusTypeMap = new HashMap<>();
    private HashMap<String, AnimeAgeRatingType> animeAgeRatingTypeMap = new HashMap<>();
    private HashMap<String, AnimeSeasonType> animeSeasonTypeMap = new HashMap<>();
    private HashMap<String, AnimeSourceType> animeSourceTypeMap = new HashMap<>();

    private HashMap<String, ProducerType> producerTypeMap = new HashMap<>();
    private HashMap<Integer, Producer> producerMap = new HashMap<>();

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    @Autowired
    private Map<String, GenreType> genreTypeMap;

    @Autowired
    private HashMap<String, RelatedType> relatedTypeMap;

    @Autowired
    private AnimeDao animeDao;

    @Autowired
    private MangaDao mangaDao;

    @Autowired
    private MangaScraper mangaScraper;

    public AnimeScraper() {}

    /*
     * Initializes HashMaps based on Anime-related PairMappings
     */
    @PostConstruct
    private void initializeMaps() {
        fillNameToElemMap(animeTypeMap, AnimeType.class);
        fillNameToElemMap(animeStatusTypeMap, AnimeStatusType.class);
        fillNameToElemMap(animeSeasonTypeMap, AnimeSeasonType.class);
        fillNameToElemMap(animeSourceTypeMap, AnimeSourceType.class);
        fillNameToElemMap(producerTypeMap, ProducerType.class);
        fillIdToElemMap(producerMap, Producer.class);

        // Done separately, because AnimeAgeRating is a special
        // subtype of LookupTable with a 3rd parameter (a description)
        initializeAnimeAgeRatingTypeMap();
    }

    /*
     * Initializes the AnimeAgeRatingTypeMap by filling it with
     * values from the AnimeAgeRatingType DB table
     */
    private void initializeAnimeAgeRatingTypeMap() {
        List<AnimeAgeRatingType> types = genericDao.getTableRows(AnimeAgeRatingType.class);
        animeAgeRatingTypeMap = new HashMap<>();

        // MAL generally separates the age rating and description with a dash,
        // but it specifically separates with a space for R-17+
        for (AnimeAgeRatingType type : types) {
            String nameAndDesc;
            if (type.getDescription().isEmpty()) {
                nameAndDesc = type.getName();
            } else {
                String separator = type.getName().equals("R - 17+") ? " " : " - ";
                nameAndDesc = type.getName() + separator + type.getDescription();
            }

            animeAgeRatingTypeMap.put(nameAndDesc, type);
        }
    }

    private Anime createAnime(AnimeDetailsPage page, int animeId) {
        Anime anime = new Anime();
        anime.setId(animeId);

        // Set titles
        anime.setMainTitle(page.parseMainTitle());
        anime.setEnglishTitle(page.parseEnglishTitle());
        anime.setJapaneseTitle(page.parseJapaneseTitle());

        // Set type, status, age rating, season, and source type ids
        anime.setAnimeType(animeTypeMap.get(page.parseType()));
        anime.setAnimeStatusType(animeStatusTypeMap.get(page.parseStatus()));
        anime.setAnimeAgeRatingType(animeAgeRatingTypeMap.get(page.parseRating()));
        anime.setAnimeSeasonType(animeSeasonTypeMap.get(page.parsePremiered()));
        anime.setAnimeSourceType(animeSourceTypeMap.get(page.parseSource()));

        // Set episode information
        String episodeStr = page.parseEpisodes();
        int numEpisodes = episodeStr.equals("Unknown") ? 0 : Integer.valueOf(episodeStr);
        anime.setNumEpisodes(numEpisodes);

        int episodeDuration = miscUtils.convertTimeDuration(page.parseDuration());
        anime.setEpisodeDuration(episodeDuration);

        // Set airing dates
        String[] malDates = page.parseAired().split(" to ");

        Calendar startDateDB = miscUtils.convertDate(malDates[0]);
        anime.setStartDate(startDateDB);

        if (malDates.length == 1) {
            anime.setEndDate(startDateDB);
        } else {
            Calendar endDateDB = miscUtils.convertDate(malDates[1]);
            anime.setEndDate(endDateDB);
        }

        // Set broadcast, synopsis, and background
        anime.setBroadcast(page.parseBroadcast());
        anime.setSynopsis(page.parseSynopsis());

        String backgroundStr = page.parseBackground();
        if (StringUtils.beginsWithIgnoreCase(backgroundStr,
                "No background information has been added to this title.")) {
            backgroundStr = "";
        }

        anime.setBackground(backgroundStr);

        // Set genres
        List<String> genres = page.parseGenres();
        for (String genre : genres) {
            anime.getGenreTypes().add(genreTypeMap.get(genre));
        }

        // Set producer ids
        List<Integer> studioIds = page.parseStudios();
        for (int sId : studioIds) {
            anime.getProducerToRole().put(producerMap.get(sId), producerTypeMap.get("Studio"));
        }

        List<Integer> producerIds = page.parseProducers();
        for (int pId : producerIds) {
            anime.getProducerToRole().put(producerMap.get(pId), producerTypeMap.get("Producer"));
        }

        List<Integer> licensorIds = page.parseLicensors();
        for (int lId : licensorIds) {
            anime.getProducerToRole().put(producerMap.get(lId), producerTypeMap.get("Licensor"));
        }

        return anime;
    }

    private List<AnimeSynonymTitle> createAnimeSynonymTitles(AnimeDetailsPage page, Anime anime) {
        String[] synonymTitles = page.parseSynonymTitles();
        List<AnimeSynonymTitle> titles = new ArrayList<>();

        for (String name : synonymTitles) {
            titles.add(new AnimeSynonymTitle(anime, name));
        }

        return titles;
    }

    private void addRelatedAnimes(Anime anime, List<String> relatedAnimes, RelatedType relatedType) {
        for (String relatedPath : relatedAnimes) {
            int relatedId = Integer.valueOf(relatedPath.split("/")[1]);

            // We must insert the id, path pair int the map because
            // the MAL's anime index may not be up to date with what
            // is actually available
            animeIdToPathMap.put(relatedId, relatedPath);

            Anime relatedAnime = animeDao.getAnime(relatedId);
            if (relatedAnime == null) {
                relatedAnime = convert(relatedId);
            }

            anime.getRelatedAnimes().put(relatedAnime, relatedType);
        }
    }

    private void addMangaAdaptations(Anime anime, List<String> relatedMangas) {
        for (String relatedPath : relatedMangas) {
            int relatedId = Integer.valueOf(relatedPath.split("/")[1]);

            mangaIdToPathMap.put(relatedId, relatedPath);

            Manga relatedManga = mangaDao.getManga(relatedId);
            if (relatedPath == null) {
                relatedManga = mangaScraper.convert(relatedId);
            }

            anime.getMangaAdaptations().add(relatedManga);
        }
    }

    public Anime convert(int animeId) {
        String path = animeIdToPathMap.get(animeId);
        Document doc = parseHtml(path);
        AnimeDetailsPage page = new AnimeDetailsPage(doc);

        if (page.isEmptyPage()) {
            return null;
        }

        Anime anime = createAnime(page, animeId);
        genericDao.saveOrUpdateMalMapping(anime);

        List<AnimeSynonymTitle> titles = createAnimeSynonymTitles(page, anime);
        for (AnimeSynonymTitle title : titles) {
            genericDao.saveOrUpdateMalMapping(title);
        }

        addMangaAdaptations(anime, page.parseAdaptations());
        addRelatedAnimes(anime, page.parsePrequels(), relatedTypeMap.get("Prequel"));
        addRelatedAnimes(anime, page.parseSequels(), relatedTypeMap.get("Sequel"));
        addRelatedAnimes(anime, page.parseSideStories(), relatedTypeMap.get("Side story"));
        addRelatedAnimes(anime, page.parseParentStories(), relatedTypeMap.get("Parent story"));
        addRelatedAnimes(anime, page.parseSpinoffs(), relatedTypeMap.get("Spin-off"));
        addRelatedAnimes(anime, page.parseSummaries(), relatedTypeMap.get("Summary"));
        addRelatedAnimes(anime, page.parseAlternativeSettings(), relatedTypeMap.get("Alternative setting"));
        addRelatedAnimes(anime, page.parseAlternativeVersions(), relatedTypeMap.get("Alternative version"));
        addRelatedAnimes(anime, page.parseOthers(), relatedTypeMap.get("Other"));

        genericDao.saveOrUpdateMalMapping(anime);
        System.out.println("Converted anime with path " +  path);
        return anime;
    }
}
