package hibernateUtils.hibernateConverters.mangaConverters;

import com.amazonaws.util.StringUtils;
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
    private HashMap<String, MangaType> mangaTypeMap = new HashMap<>();
    private HashMap<String, MangaStatusType> mangaStatusTypeMap = new HashMap<>();
    private HashMap<String, Magazine> magazineMap = new HashMap<>();

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    public MangaConverter() {}

    @PostConstruct
    private void initializeMaps() {
        fillNameToElemMap(mangaTypeMap, MangaType.class);
        fillNameToElemMap(mangaStatusTypeMap, MangaStatusType.class);
        fillNameToElemMap(magazineMap, Magazine.class);
    }

    public void convert(int mangaId) {
        String path = mangaIdToPathMap.get(mangaId);
        Document doc = parseHtml(path);
        MangaDetailsPage page = new MangaDetailsPage(doc);

        if (page.isEmptyPage()) {
            return;
        }

        Manga manga = new Manga();
        manga.setId(mangaId);

        // Set the type and status type ids
        manga.setMangaType(mangaTypeMap.get(page.parseType()));
        manga.setMangaStatusType(mangaStatusTypeMap.get(page.parseStatus()));

        // Set publisher
        manga.setMagazine(magazineMap.get(page.parseSerialization()));

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

        String backgroundStr = page.parseBackground();
        if (StringUtils.beginsWithIgnoreCase(backgroundStr,
                "No background information has been added to this title.")) {
            backgroundStr = "";
        }
        manga.setBackground(backgroundStr);

        hibernateUtils.updateMalMapping(mangaId, manga);
    }
}
