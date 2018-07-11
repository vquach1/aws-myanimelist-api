package scrapers.persons;

import scrapers.abstracts.Scraper;
import hibernateUtils.mappings.persons.Person;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pageTypes.persons.PersonDetailsPage;

import java.util.Arrays;
import java.util.HashMap;

public class PersonScraper extends Scraper {
    @Autowired
    @Qualifier("peopleIdToPathMap")
    private HashMap<Integer, String> peopleIdToPathMap;

    public PersonScraper() {}

    public Person convert(int personId) {
        String path = peopleIdToPathMap.get(personId);
        Document doc = parseHtml(path);;
        PersonDetailsPage page = new PersonDetailsPage(doc);

        if (page.isEmptyPage()) {
            return null;
        }

        Person person = new Person();
        person.setId(personId);

        // Parse English and foreign names. To simplify things,
        // everything that's not the first name (e.g. middle names)
        // will be lumped into the last name
        String[] fullName = page.parseName().split(" ");
        String englishLastName;

        if (fullName.length == 1) {
            englishLastName = "";
        } else {
            String[] englishLastNameArr = Arrays.copyOfRange(fullName, 1, fullName.length);
            englishLastName = String.join(" ", englishLastNameArr);
        }

        person.setEnglishFirstName(fullName[0]);
        person.setEnglishLastName(englishLastName);

        person.setForeignFirstName(page.parseGivenName());
        person.setForeignLastName(page.parseFamilyName());

        // Parse birthday
        String birthday = page.parseBirthday();
        if (birthday.equals("Unknown")) {
            birthday = "";
        }

        person.setBirthday(birthday);

        // Parse website, member favorites, and information in section 'More'
        person.setWebsite(page.parseWebsite());
        person.setFavorites(page.parseMemberFavorites());
        person.setOtherInfo(page.parseMore());

        genericDao.saveOrUpdateMalMapping(person);
        System.out.println("    Converted " + path);
        return person;
    }
}
