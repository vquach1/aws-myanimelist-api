package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.MalCharacter;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scrapers.CharacterDetailsPage;

import java.util.Arrays;
import java.util.HashMap;

public class CharacterConverter extends Converter {
    @Autowired
    @Qualifier("characterIdToPathMap")
    private HashMap<Integer, String> characterIdToPathMap;

    public CharacterConverter() {}

    public void convert(int characterId) {
        String path = characterIdToPathMap.get(characterId);
        Document doc = parseHtml(path);
        CharacterDetailsPage page = new CharacterDetailsPage(doc);
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

        hibernateUtils.updateMalMapping(characterId, character);
    }
}
