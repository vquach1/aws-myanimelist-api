package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.Person;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrapers.PersonDetailsPage;

import java.util.Arrays;

public class PersonConverter extends Converter {
    public PersonConverter(String bucketName) {
        super(bucketName);
    }

    public void convert(String key) {
        String[] keySplit = key.split("-");
        int id = Integer.parseInt(keySplit[keySplit.length - 1]);

        String rawHtml = s3Utils.readObject(bucketName, key);
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        PersonDetailsPage page = new PersonDetailsPage(doc);

        String[] fullName = page.parseName().split(" ");
        String englishFirstName = fullName[0];;

        // Parse English last name. May be multiple
        String englishLastName;

        if (fullName.length == 1) {
            englishLastName = "";
        } else {
            String[] englishLastNameArr = Arrays.copyOfRange(fullName, 1, fullName.length);
            englishLastName = String.join(" ", englishLastNameArr);
        }

        String foreignFirstName = page.parseGivenName();
        String foreignLastName = page.parseFamilyName();

        String birthday = page.parseBirthday();
        if (birthday.equals("Unknown")) {
            birthday = "";
        }

        String website = page.parseWebsite();
        int favorites = page.parseMemberFavorites();
        String otherInfo = page.parseMore();

        Person person = new Person(id, englishFirstName, englishLastName, foreignFirstName,
                foreignLastName, birthday, website, favorites, otherInfo);

        hibernateUtils.updateMalMapping(id, person);
    }
}
