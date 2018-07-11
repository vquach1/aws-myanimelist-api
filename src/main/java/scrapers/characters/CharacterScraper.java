package scrapers.characters;

import scrapers.abstracts.Scraper;
import scrapers.animes.AnimeScraper;
import scrapers.mangas.MangaScraper;
import hibernateUtils.mappings.characters.MalCharacter;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.characters.CharacterDetailsPage;

import java.util.Arrays;
import java.util.HashMap;

public class CharacterScraper extends Scraper {
    @Autowired
    @Qualifier("characterIdToPathMap")
    private HashMap<Integer, String> characterIdToPathMap;

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    @Autowired
    @Qualifier("mangaIdToPathMap")
    private HashMap<Integer, String> mangaIdToPathMap;

    @Autowired
    private AnimeScraper animeConverter;

    @Autowired
    private MangaScraper mangaConverter;

    public CharacterScraper() {}

    public MalCharacter convert(int characterId) {
        String path = characterIdToPathMap.get(characterId);
        Document doc = parseHtml(path);
        CharacterDetailsPage page = new CharacterDetailsPage(doc);

        if (page.isEmptyPage()) {
            return null;
        }

        MalCharacter character = new MalCharacter();
        character.setId(characterId);

        // Extract favorites
        int favorites = page.parseMemberFavorites();
        character.setFavorites(favorites);

        // Extract English names
        String[] fullName = page.parseName().split(" ");
        String englishFirstName = fullName[0];
        String englishLastName;

        if (fullName.length == 1) {
            englishLastName = "";
        } else {
            String[] englishLastNameArr = Arrays.copyOfRange(fullName, 1, fullName.length);
            englishLastName = String.join(" ", englishLastNameArr);
        }

        character.setEnglishFirstName(englishFirstName);
        character.setEnglishLastName(englishLastName);

        // Extract foreign names
        String foreignNames = page.parseForeignName();
        character.setForeignNames(foreignNames);

        // Extract biography
        String biography = page.parseBiography();
        biography = StringUtils.removeEnd(biography, "<br><br>");
        if (biography.equals("No biography written.")) {
            biography = "";
        }

        character.setBiography(biography);

        genericDao.saveOrUpdateMalMapping(character);
        System.out.println("    Converted " + path);
        return character;
    }
}
