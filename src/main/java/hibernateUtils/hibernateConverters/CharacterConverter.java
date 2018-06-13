package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.MalCharacter;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrapers.CharacterDetailsPage;

import java.util.Arrays;

public class CharacterConverter extends Converter {
    public CharacterConverter(String bucketName) {
        super(bucketName);
    }

    public void convert(String key) {
        String[] keySplit = key.split("-");
        int id = Integer.parseInt(keySplit[keySplit.length - 1]);

        String rawHtml = s3Utils.readObject(bucketName, key);
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        CharacterDetailsPage page = new CharacterDetailsPage(doc);

        // Extract favorites
        int favorites = page.parseMemberFavorites();

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

        // Extract foreign names
        String foreignNames = page.parseForeignName();

        // Extract biography
        String biography = page.parseBiography();

        biography = StringUtils.removeEnd(biography, "<br><br>");
        if (biography.equals("No biography written.")) {
            biography = "";
        }

        MalCharacter character = new MalCharacter(id, favorites, englishFirstName, englishLastName,
                foreignNames, biography);

        hibernateUtils.updateMalMapping(id, character);
    }
}
