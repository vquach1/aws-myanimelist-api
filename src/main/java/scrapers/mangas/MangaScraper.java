package scrapers.mangas;

import com.amazonaws.util.StringUtils;
import scrapers.abstracts.Scraper;
import hibernateUtils.mappings.mangas.Magazine;
import hibernateUtils.mappings.mangas.Manga;
import hibernateUtils.mappings.lookupTables.MangaStatusType;
import hibernateUtils.mappings.lookupTables.MangaType;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.mangas.MangaDetailsPage;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.HashMap;

public class MangaScraper extends Scraper {
    private HashMap<String, MangaType> mangaTypeMap = new HashMap<>();
    private HashMap<String, MangaStatusType> mangaStatusTypeMap = new HashMap<>();
    private HashMap<String, Magazine> magazineMap = new HashMap<>();

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    public MangaScraper() {}

    @PostConstruct
    private void initializeMaps() {
        fillNameToElemMap(mangaTypeMap, MangaType.class);
        fillNameToElemMap(mangaStatusTypeMap, MangaStatusType.class);
        fillNameToElemMap(magazineMap, Magazine.class);
    }

    public Manga convert(int mangaId) {
        String path = mangaIdToPathMap.get(mangaId);
        Document doc = parseHtml(path);
        MangaDetailsPage page = new MangaDetailsPage(doc);

        if (page.isEmptyPage()) {
            return null;
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

        genericDao.saveOrUpdateMalMapping(manga);
        return manga;
    }
}
