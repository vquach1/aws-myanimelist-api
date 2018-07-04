package hibernateUtils.hibernateConverters.animeConverters;

import com.amazonaws.util.StringUtils;
import hibernateUtils.hibernateConverters.abstractConverters.Converter;
import hibernateUtils.hibernateMappings.GenreType;
import hibernateUtils.hibernateMappings.animeMappings.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scrapers.animeScrapers.AnimeDetailsPage;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AnimeConverter extends Converter {
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
    private HashMap<Integer, GenreType> genreTypeMap;

    public AnimeConverter() {}

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
        // subtype of PairMapping with a 3rd parameter (a description)
        initializeAnimeAgeRatingTypeMap();
    }

    /*
     * Initializes the AnimeAgeRatingTypeMap by filling it with
     * values from the AnimeAgeRatingType DB table
     */
    private void initializeAnimeAgeRatingTypeMap() {
        List<AnimeAgeRatingType> types = hibernateUtils.getTableRows(AnimeAgeRatingType.class);
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

    public void convert(int animeId) {
        String path = animeIdToPathMap.get(animeId);
        Document doc = parseHtml(path);
        AnimeDetailsPage page = new AnimeDetailsPage(doc);

        if (page.isEmptyPage()) {
            return;
        }

        Anime anime = new Anime();
        anime.setId(animeId);

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
        List<Integer> genreIds = page.parseGenres();
        for (int gId : genreIds) {
            anime.getGenreTypes().add(genreTypeMap.get(gId));
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

        hibernateUtils.updateMalMapping(animeId, anime);
    }
}
