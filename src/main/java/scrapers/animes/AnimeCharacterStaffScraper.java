package scrapers.animes;

import scrapers.abstracts.Scraper;
import scrapers.characters.CharacterScraper;
import scrapers.persons.PersonScraper;
import hibernateUtils.daos.AnimeDao;
import hibernateUtils.daos.MalCharacterDao;
import hibernateUtils.daos.PersonDao;
import hibernateUtils.mappings.animes.Anime;
import hibernateUtils.mappings.characters.MalCharacter;
import hibernateUtils.mappings.joinTables.AnimeCharacter;
import hibernateUtils.mappings.joinTables.AnimeStaffRole;
import hibernateUtils.mappings.joinTables.VoiceActorRole;
import hibernateUtils.mappings.lookupTables.AnimeStaffRoleType;
import hibernateUtils.mappings.lookupTables.LanguageType;
import hibernateUtils.mappings.lookupTables.MalCharacterRoleType;
import hibernateUtils.mappings.persons.Person;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.animes.AnimeCharacterStaffPage;
import pageTypes.scraperDataTypes.AnimeCharacterInfo;
import pageTypes.scraperDataTypes.AnimeStaffInfo;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimeCharacterStaffScraper extends Scraper {
    private HashMap<String, MalCharacterRoleType> characterRoleTypeMap = new HashMap<>();
    private HashMap<String, AnimeStaffRoleType> animeStaffRoleTypeMap = new HashMap<>();
    private HashMap<String, LanguageType> languageTypeMap = new HashMap<>();

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    @Autowired
    @Qualifier("characterIdToPathMap")
    private HashMap<Integer, String> characterIdToPathMap;

    @Autowired
    @Qualifier("peopleIdToPathMap")
    private HashMap<Integer, String> peopleIdToPathMap;

    @Autowired
    private CharacterScraper characterScraper;

    @Autowired
    private PersonScraper personScraper;

    @Autowired
    private AnimeDao animeDao;

    @Autowired
    private MalCharacterDao malCharacterDao;

    @Autowired
    private PersonDao personDao;

    public AnimeCharacterStaffScraper() {}

    @PostConstruct
    private void initializeMaps() {
        fillNameToElemMap(characterRoleTypeMap, MalCharacterRoleType.class);
        fillNameToElemMap(animeStaffRoleTypeMap, AnimeStaffRoleType.class);
        fillNameToElemMap(languageTypeMap, LanguageType.class);
    }

    public void convert(int animeId) {
        // TODO: Fix dependencies to check for anime before seeking a path
        String path = animeIdToPathMap.get(animeId) + "/characters";
        System.out.println("Converting " + path);
        Document doc = parseHtml(path);
        AnimeCharacterStaffPage page = new AnimeCharacterStaffPage(doc);

        if (page.isEmptyPage()) {
            return;
        }

        List<AnimeCharacterInfo> characterInfo = page.parseCharacters();
        List<AnimeCharacter> animeCharacters = new ArrayList<>();
        List<VoiceActorRole> voiceActorRoles = new ArrayList<>();
        Anime anime = animeDao.getAnime(animeId);

        // For each character in the anime, add...
        // -The character's role type (Main/Supporting)
        // -The character's voice actors
        for (AnimeCharacterInfo info : characterInfo) {
            String charPath = info.getCharacterPath();
            int charId = Integer.valueOf(charPath.split("/")[1]);
            characterIdToPathMap.put(charId, charPath);

            MalCharacter malChar = malCharacterDao.getMalCharacter(charId);
            if (malChar == null) {
                malChar = characterScraper.convert(charId);
            }

            MalCharacterRoleType charRoleType = characterRoleTypeMap.get(info.getCharacterRole());
            animeCharacters.add(new AnimeCharacter(anime, malChar, charRoleType));

            Map<String, String> vaToLang = info.getVoiceActorPathToLanguage();
            for (String personPath : vaToLang.keySet()) {
                int personId = Integer.valueOf(personPath.split("/")[1]);
                peopleIdToPathMap.put(personId, personPath);

                Person person = personDao.getPerson(personId);
                if (person == null) {
                    person = personScraper.convert(personId);
                }

                LanguageType langType = languageTypeMap.get(vaToLang.get(personPath));
                voiceActorRoles.add(new VoiceActorRole(person, malChar, langType));
            }
        }

        malCharacterDao.addVoiceActors(voiceActorRoles);
        animeDao.addCharacterRoles(animeCharacters);

        List<AnimeStaffInfo> staffInfo = page.parseStaff();
        List<AnimeStaffRole> animeStaffRoles = new ArrayList<>();

        // For each staff member in the anime, add the member's roles
        for (AnimeStaffInfo info : staffInfo) {
            String personPath = info.getPersonPath();
            int personId = Integer.valueOf(personPath.split("/")[1]);
            peopleIdToPathMap.put(personId, personPath);

            Person person = personDao.getPerson(personId);
            if (person == null) {
                person = personScraper.convert(personId);
            }

            for (String role : info.getRoles()) {
                AnimeStaffRoleType roleType = animeStaffRoleTypeMap.get(role);

                if (roleType == null) {
                    System.out.println("Key not present for <" + role + ">");
                }

                animeStaffRoles.add(new AnimeStaffRole(anime, person, roleType));
            }
        }

        animeDao.addStaffRoles(animeStaffRoles);
    }
}
