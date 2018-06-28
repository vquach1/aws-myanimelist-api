package hibernateUtils.hibernateConverters.mangaConverters;

import hibernateUtils.hibernateConverters.abstractConverters.Converter;
import hibernateUtils.hibernateMappings.mangaMappings.Magazine;
import hibernateUtils.hibernateMappings.mangaMappings.Manga;
import hibernateUtils.hibernateMappings.mangaMappings.MangaStatusType;
import hibernateUtils.hibernateMappings.mangaMappings.MangaType;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scrapers.mangaScrapers.MangaDetailsPage;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MangaConverter extends Converter {
    private HashMap<String, Integer> mangaTypeMap;
    private HashMap<String, Integer> mangaStatusTypeMap;
    private HashMap<String, Integer> magazineMap;

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    public MangaConverter() {}

    @PostConstruct
    private void initializeMaps() {
        mangaTypeMap = fillNameToIdMap(MangaType.class);
        mangaStatusTypeMap = fillNameToIdMap(MangaStatusType.class);
        magazineMap = fillNameToIdMap(Magazine.class);
    }

    private int getMapping(HashMap<String, Integer> map, String name) {
        if (!map.containsKey(name)) {
            System.out.println("New name found: " + name);
        }

        return map.containsKey(name) ? map.get(name) : 0;
    }

    public void convert(int mangaId) {
        String path = mangaIdToPathMap.get(mangaId);
        Document doc = parseHtml(path);

        MangaDetailsPage page = new MangaDetailsPage(doc);
        Manga manga = new Manga();

        try {
            manga.setId(mangaId);

            // Set the type and status type ids
            String type = page.parseType();
            manga.setMangaTypeId(getMapping(mangaTypeMap, type));

            String status = page.parseStatus();
            manga.setMangaStatusTypeId(getMapping(mangaStatusTypeMap, status));

            // Set publisher
            String serializationStr = page.parseSerialization();
            int magazineId = getMapping(magazineMap, serializationStr);
            manga.setMagazineId(magazineId);

            // Set publishing statistics
            String volumeStr = page.parseVolumes();
            int numVolumes = volumeStr.equals("Unknown") ? 0 : Integer.valueOf(volumeStr);
            manga.setVolumes(numVolumes);

            String chapterStr = page.parseChapters();
            int numChapters = chapterStr.equals("Unknown") ? 0 : Integer.valueOf(chapterStr);
            manga.setChapters(numChapters);

            // Set publishing dates
            String[] malDates = page.parsePublished().split(" to ");

            Calendar startDateDB = miscUtils.convertDate(malDates[0]);
            manga.setStartDate(startDateDB);

            if (malDates.length == 1) {
                manga.setEndDate(startDateDB);
            } else {
                Calendar endDateDB = miscUtils.convertDate(malDates[1]);
                manga.setEndDate(endDateDB);
            }

            // Set synopsis and background
            manga.setSynopsis(page.parseSynopsis());
            manga.setBackground(page.parseBackground());

            hibernateUtils.updateMalMapping(mangaId, manga);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
