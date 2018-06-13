package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AnimeConverter(String bucketName) {
        super(bucketName);
    }

    // Think about making a FactoryBean to generate instances of AnimeConverters
    @PostConstruct
    private void initializeAnimeTypeMap() {
        animeTypeMap = new HashMap<>();

        List<AnimeType> types = hibernateUtils.getAnimeTypes();

        for (AnimeType type : types) {
            animeTypeMap.put(type.getName(), type.getId());
        }
    }

    @PostConstruct
    private void initializeAnimeStatusTypeMap() {
        animeStatusTypeMap = new HashMap<>();

        List<AnimeStatusType> types = hibernateUtils.getAnimeStatusTypes();

        for (AnimeStatusType type: types) {
            animeStatusTypeMap.put(type.getName(), type.getId());
        }
    }

    @PostConstruct
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

    @PostConstruct
    private void initializeAnimeSeasonTypeMap() {
        animeSeasonTypeMap = new HashMap<>();

        List<AnimeSeasonType> types = hibernateUtils.getAnimeSeasonTypes();

        for (AnimeSeasonType type : types) {
            animeSeasonTypeMap.put(type.getName(), type.getId());
        }
    }

    @PostConstruct
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

    public void convert(String key) {
        String[] keySplit = key.split("-");
        int id = Integer.parseInt(keySplit[keySplit.length - 1]);

        String rawHtml = s3Utils.readObject(bucketName, key);
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        AnimeDetailsPage page = new AnimeDetailsPage(doc);
        Anime anime = new Anime();

        anime.setId(id);

        // Set type id
        String type = page.parseType();
        int animeTypeId = getMapping(animeTypeMap, type);
        anime.setAnimeTypeId(animeTypeId);

        // Set status type id
        String status = page.parseStatus();
        int animeStatusTypeId = getMapping(animeStatusTypeMap, status);
        anime.setAnimeStatusTypeId(animeStatusTypeId);

        // Set age rating type id
        String rating = page.parseRating();
        int animeAgeRatingTypeId = getMapping(animeAgeRatingTypeMap, rating);
        anime.setAnimeAgeRatingTypeId(animeAgeRatingTypeId);

        // Set season type id
        String seasonPremiered = page.parsePremiered();
        int animeSeasonTypeId = getMapping(animeSeasonTypeMap, seasonPremiered);
        anime.setAnimeSeasonTypeId(animeSeasonTypeId);

        // Set source type id
        String source = page.parseSource();
        int animeSourceTypeId = getMapping(animeSourceTypeMap, source);
        anime.setAnimeSourceTypeId(animeSourceTypeId);

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

        // Set broadcast
        anime.setBroadcast(page.parseBroadcast());

        // Set synopsis and background
        anime.setSynopsis(page.parseSynopsis());
        anime.setBackground(page.parseBackground());

        hibernateUtils.updateMalMapping(id, anime);
    }
}
