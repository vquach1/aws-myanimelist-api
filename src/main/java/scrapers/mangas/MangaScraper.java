package scrapers.mangas;

import com.amazonaws.util.StringUtils;
import hibernateUtils.daos.AnimeDao;
import hibernateUtils.daos.MangaDao;
import hibernateUtils.daos.PersonDao;
import hibernateUtils.mappings.animes.Anime;
import hibernateUtils.mappings.animes.AnimeSynonymTitle;
import hibernateUtils.mappings.joinTables.MangaAuthor;
import hibernateUtils.mappings.joinTables.MangaRelated;
import hibernateUtils.mappings.lookupTables.*;
import hibernateUtils.mappings.mangas.MangaSynonymTitle;
import hibernateUtils.mappings.persons.Person;
import scrapers.abstracts.Scraper;
import hibernateUtils.mappings.mangas.Magazine;
import hibernateUtils.mappings.mangas.Manga;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.mangas.MangaDetailsPage;
import scrapers.animes.AnimeScraper;
import scrapers.persons.PersonScraper;

import javax.annotation.PostConstruct;
import java.util.*;

public class MangaScraper extends Scraper {
    private Map<String, MangaType> mangaTypeMap = new HashMap<>();
    private Map<String, MangaStatusType> mangaStatusTypeMap = new HashMap<>();
    private Map<String, MangaAuthorType> mangaAuthorTypeMap = new HashMap<>();
    private Map<String, Magazine> magazineMap = new HashMap<>();

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    @Autowired
    @Qualifier("peopleIdToPathMap")
    private HashMap<Integer, String> peopleIdToPathMap;

    @Autowired
    private Map<String, GenreType> genreTypeMap;

    @Autowired
    private HashMap<String, RelatedType> relatedTypeMap;

    @Autowired
    private AnimeDao animeDao;

    @Autowired
    private MangaDao mangaDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private AnimeScraper animeScraper;

    @Autowired
    private PersonScraper personScraper;

    public MangaScraper() {}

    @PostConstruct
    private void initializeMaps() {
        fillNameToElemMap(mangaTypeMap, MangaType.class);
        fillNameToElemMap(mangaStatusTypeMap, MangaStatusType.class);
        fillNameToElemMap(mangaAuthorTypeMap, MangaAuthorType.class);
        fillNameToElemMap(magazineMap, Magazine.class);
    }

    private Manga createManga(MangaDetailsPage page, int mangaId) {
        Manga manga = new Manga();
        manga.setId(mangaId);

        // Set titles
        manga.setMainTitle(page.parseMainTitle());
        manga.setEnglishTitle(page.parseEnglishTitle());
        manga.setJapaneseTitle(page.parseJapaneseTitle());

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

        // Set genres
        List<String> genres = page.parseGenres();
        for (String genre: genres) {
            manga.getGenreTypes().add(genreTypeMap.get(genre));
        }

        // Set synopsis and background
        manga.setSynopsis(page.parseSynopsis());

        String backgroundStr = page.parseBackground();
        if (StringUtils.beginsWithIgnoreCase(backgroundStr,
                "No background information has been added to this title.")) {
            backgroundStr = "";
        }

        manga.setBackground(backgroundStr);

        return manga;
    }

    private List<MangaSynonymTitle> createMangaSynonymTitles(MangaDetailsPage page, Manga manga) {
        String[] synonymTitles = page.parseSynonymTitles();
        List<MangaSynonymTitle> titles = new ArrayList<>();

        for (String name : synonymTitles) {
            titles.add(new MangaSynonymTitle(manga, name));
        }

        return titles;
    }

    private void addRelatedMangas(List<MangaRelated> allMangaRelated, Manga manga,
                                  List<String> relatedMangaPaths, RelatedType relatedType) {
        for (String relatedPath : relatedMangaPaths) {
            int relatedId = Integer.valueOf(relatedPath.split("/")[1]);

            // We must insert the id, path pair int the map because
            // the MAL's anime index may not be up to date with what
            // is actually available
            mangaIdToPathMap.put(relatedId, relatedPath);

            Manga relatedManga = mangaDao.getManga(relatedId);
            if (relatedManga == null) {
                relatedManga = convert(relatedId);
            }

            allMangaRelated.add(new MangaRelated(manga, relatedManga, relatedType));
        }
    }

    private void addAnimeAdaptations(Manga manga, List<String> relatedAnimes) {
        for (String relatedPath : relatedAnimes) {
            int relatedId = Integer.valueOf(relatedPath.split("/")[1]);

            animeIdToPathMap.put(relatedId, relatedPath);

            Anime relatedAnime = animeDao.getAnime(relatedId);
            if (relatedAnime == null) {
                relatedAnime = animeScraper.convert(relatedId);
            }

            manga.getAnimeAdaptations().add(relatedAnime);
            animeDao.addMangaAdaptation(relatedAnime, manga);
        }
    }

    private List<MangaAuthor> createMangaAuthors(Manga manga, Map<String, String> authorRoles) {
        List<MangaAuthor> mangaAuthors = new ArrayList<>();

        for (String personPath : authorRoles.keySet()) {
            int personId = Integer.valueOf(personPath.split("/")[1]);
            peopleIdToPathMap.put(personId, personPath);

            Person person = personDao.getPerson(personId);
            if (person == null) {
                person = personScraper.convert(personId);
            }

            String role = authorRoles.get(personPath);
            MangaAuthorType mangaAuthorType = mangaAuthorTypeMap.get(role);
            mangaAuthors.add(new MangaAuthor(manga, person, mangaAuthorType));
        }

        return mangaAuthors;
    }

    public Manga convert(int mangaId) {
        String path = mangaIdToPathMap.get(mangaId);
        Document doc = parseHtml(path);
        MangaDetailsPage page = new MangaDetailsPage(doc);

        System.out.println("Converting manga " + path);

        if (page.isEmptyPage()) {
            return null;
        }

        Manga manga = createManga(page, mangaId);
        genericDao.saveOrUpdateMalMapping(manga);

        List<MangaSynonymTitle> titles = createMangaSynonymTitles(page, manga);
        for (MangaSynonymTitle title : titles) {
            genericDao.saveOrUpdateMalMapping(title);
        }

        Map<String, String> authors = page.parseAuthors();
        List<MangaAuthor> mangaAuthors = createMangaAuthors(manga, authors);
        mangaDao.addMangaAuthors(mangaAuthors);

        addAnimeAdaptations(manga, page.parseAdaptations());

        List<MangaRelated> allMangaRelated = new ArrayList<>();
        addRelatedMangas(allMangaRelated, manga,
                page.parsePrequels(), relatedTypeMap.get("Prequel"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseSequels(), relatedTypeMap.get("Sequel"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseSideStories(), relatedTypeMap.get("Side story"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseParentStories(), relatedTypeMap.get("Parent story"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseSpinoffs(), relatedTypeMap.get("Spin-off"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseSummaries(), relatedTypeMap.get("Summary"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseAlternativeSettings(), relatedTypeMap.get("Alternative setting"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseAlternativeVersions(), relatedTypeMap.get("Alternative version"));
        addRelatedMangas(allMangaRelated, manga,
                page.parseOthers(), relatedTypeMap.get("Other"));

        mangaDao.addMangaRelated(allMangaRelated);

        genericDao.saveOrUpdateMalMapping(manga);
        return manga;
    }
}
