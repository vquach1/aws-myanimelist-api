package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scrapers.AnimeDetailsPage;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AnimeConverter extends Converter {
    private HashMap<String, Integer> animeTypeMap;
    private HashMap<String, Integer> animeStatusTypeMap;
    private HashMap<String, Integer> animeAgeRatingTypeMap;
    private HashMap<String, Integer> animeSeasonTypeMap;
    private HashMap<String, Integer> animeSourceTypeMap;

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    public AnimeConverter() {}

    @PostConstruct
    private void initializeMaps() {
        initializeAnimeTypeMap();
        initializeAnimeStatusTypeMap();
        initializeAnimeAgeRatingTypeMap();
        initializeAnimeSeasonTypeMap();
        initializeAnimeSourceTypeMap();;
    }

    private void initializeAnimeTypeMap() {
        animeTypeMap = new HashMap<>();

        List<AnimeType> types = hibernateUtils.getAnimeTypes();

        for (AnimeType type : types) {
            animeTypeMap.put(type.getName(), type.getId());
        }
    }

    private void initializeAnimeStatusTypeMap() {
        animeStatusTypeMap = new HashMap<>();

        List<AnimeStatusType> types = hibernateUtils.getAnimeStatusTypes();

        for (AnimeStatusType type: types) {
            animeStatusTypeMap.put(type.getName(), type.getId());
        }
    }

    private void initializeAnimeAgeRatingTypeMap() {
        animeAgeRatingTypeMap = new HashMap<>();

        List<AnimeAgeRatingType> types = hibernateUtils.getAnimeAgeRatingTypes();

        // MAL generally separates the age rating and description with a dash,
        // but it specifically separates with a space for R-17+
        for (AnimeAgeRatingType type : types) {
            String separator = type.getName().equals("R - 17+") ? " " : " - ";
            String nameAndDesc = type.getName() + separator + type.getDescription();
            animeAgeRatingTypeMap.put(nameAndDesc, type.getId());
        }
    }

    private void initializeAnimeSeasonTypeMap() {
        animeSeasonTypeMap = new HashMap<>();

        List<AnimeSeasonType> types = hibernateUtils.getAnimeSeasonTypes();

        for (AnimeSeasonType type : types) {
            animeSeasonTypeMap.put(type.getName(), type.getId());
        }
    }

    private void initializeAnimeSourceTypeMap() {
        animeSourceTypeMap = new HashMap<>();

        List<AnimeSourceType> types = hibernateUtils.getAnimeSourceTypes();

        for (AnimeSourceType type : types) {
            animeSourceTypeMap.put(type.getName(), type.getId());
        }
    }

    private int getMapping(HashMap<String, Integer> map, String name) {
        return map.containsKey(name) ? map.get(name) : map.getOrDefault("Unknown", 0);
    }

    public void convert(int animeId) {
        String path = animeIdToPathMap.get(animeId);
        Document doc = parseHtml(path);
        AnimeDetailsPage page = new AnimeDetailsPage(doc);
        Anime anime = new Anime();

        anime.setId(animeId);

        // Set type, status, age rating, season, and source type ids
        String type = page.parseType();
        anime.setAnimeTypeId(getMapping(animeTypeMap, type));

        String status = page.parseStatus();
        anime.setAnimeStatusTypeId(getMapping(animeStatusTypeMap, status));

        String rating = page.parseRating();
        anime.setAnimeAgeRatingTypeId(getMapping(animeAgeRatingTypeMap, rating));

        String seasonPremiered = page.parsePremiered();
        anime.setAnimeSeasonTypeId(getMapping(animeSeasonTypeMap, seasonPremiered));

        String source = page.parseSource();
        anime.setAnimeSourceTypeId(getMapping(animeSourceTypeMap, source));

        // Set episode information
        String episodeStr = page.parseEpisodes();
        int numEpisodes = episodeStr.equals("Unknown") ? 0 : Integer.valueOf(episodeStr);
        anime.setNumEpisodes(numEpisodes);

        int episodeDuration = miscUtils.convertTimeDuration(page.parseDuration());
        anime.setEpisodeDuration(episodeDuration);

        // Set airing dates
        String[] malDates = page.parseAired().split(" to ");
        Calendar startDateDB = miscUtils.convertDate(malDates[0]);
        Calendar endDateDB = miscUtils.convertDate(malDates[1]);
        anime.setStartDate(startDateDB);
        anime.setEndDate(endDateDB);

        // Set broadcast, synopsis, and background
        anime.setBroadcast(page.parseBroadcast());
        anime.setSynopsis(page.parseSynopsis());
        anime.setBackground(page.parseBackground());

        hibernateUtils.updateMalMapping(animeId, anime);
    }
}
