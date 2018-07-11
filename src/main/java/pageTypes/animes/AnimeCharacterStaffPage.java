package pageTypes.animes;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pageTypes.abstracts.MalPage;
import pageTypes.scraperDataTypes.AnimeCharacterInfo;
import pageTypes.scraperDataTypes.AnimeStaffInfo;

import java.util.*;

/*
 * Provides HTML scraping utilities for pages of the path 'anime/{animeId}/{animeTitle}/characters'
 * Example: anime/1735/Naruto__Shippuuden/characters
 */
public class AnimeCharacterStaffPage extends MalPage {
    private static final String charVoiceActorSelector = "h2:contains(Characters & Voice Actors)";
    private static final String staffSelector = "h2:contains(Staff)";

    public AnimeCharacterStaffPage(Document doc) {
        super(doc);
    }

    /*
     * Processes the 'Characters & Voice Actors' table, and
     * returns a list of AnimeCharacterInfo where each object
     * encapsulates one character in the table
     */
    public List<AnimeCharacterInfo> parseCharacters() {
        List<AnimeCharacterInfo> infos = new ArrayList<>();
        Element charTable = nextElementSibling(charVoiceActorSelector);

        if (charTable == null) {
            return infos;
        }

        while (charTable.tagName().equals("table")) {
            Element charRow = charTable.child(0).child(0);
            Element charAndRole = charRow.child(1);
            String charPath = StringUtils.removeStart(charAndRole.child(0).attr("href"), "https://myanimelist.net/");
            String charRole = charAndRole.selectFirst("small").ownText();
            Map<String, String> voiceActors = new HashMap<>();

            // Not every character has a voice actor
            if (charRow.child(2).child(0).children().size() > 0) {
                Element vaAndLangs = charRow.child(2).child(0).child(0);

                for (Element vaRow : vaAndLangs.children()) {
                    Element va = vaRow.child(0);
                    String personPath = StringUtils.removeStart(va.child(0).attr("href"), "https://myanimelist.net/");
                    String personLang = va.selectFirst("small").ownText();
                    voiceActors.put(personPath, personLang);
                }
            }

            infos.add(new AnimeCharacterInfo(charPath, charRole, voiceActors));
            charTable = charTable.nextElementSibling();
        }

        return infos;
    }

    /*
     * Processes the 'Staff' table, and returns a list of
     * AnimeStaffInfo where each object encapsulates one
     * staff member in the table
     */
    public List<AnimeStaffInfo> parseStaff() {
        List<AnimeStaffInfo> infos = new ArrayList<>();
        Element staffTable = nextElementSibling(staffSelector);

        if (staffTable == null) {
            return infos;
        }

        while (staffTable != null && staffTable.tagName().equals("table")) {
            Element personRow = staffTable.child(0).child(0);
            Element personAndRoles = personRow.child(1);
            String personPath = StringUtils.removeStart(
                    personAndRoles.child(0).attr("href"), "https://myanimelist.net/");
            String[] personRolesArr = personAndRoles.selectFirst("small").ownText().split(", ");
            List<String> personRoles = Arrays.asList(personRolesArr);

            infos.add(new AnimeStaffInfo(personPath, personRoles));
            staffTable = staffTable.nextElementSibling();
        }

        return infos;
    }
}
