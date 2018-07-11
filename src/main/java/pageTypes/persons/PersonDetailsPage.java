package pageTypes.persons;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pageTypes.abstracts.MalPage;

/*
 * Provides HTML scraping utilities for pages of the path '/people/{personId}/{personName}'
 * Example: /people/11297/Inori_Minase
 */
public class PersonDetailsPage extends MalPage {
    private final static String givenNameSelector = "span[class='dark_text']:contains(Given name:)";
    private final static String familyNameSelector = "span[class='dark_text']:contains(Family name:)";
    private final static String birthdaySelector = "span[class='dark_text']:contains(Birthday:)";
    private final static String websiteSelector = "span[class='dark_text']:contains(Website:)";
    private final static String memberFavoritesSelector = "span[class='dark_text']:contains(Member Favorites:)";
    private final static String moreSelector = "div.people-informantion-more";

    public PersonDetailsPage(Document doc) {
        super(doc);
    }

    /*
     * Parses the full name by pulling it from the page title
     */
    public String parseName() {
        return StringUtils.removeEnd(doc.title(), " - MyAnimeList.net");
    }

    /*
     * Parses the given name (native first name) from the
     * 'Given name' row in the left column
     */
    public String parseGivenName() {
        Element givenName = parent(givenNameSelector);
        return ownText(givenName);
    }

    /*
     * Parses the family name (native last name) from the
     * from the 'Family name' row in the left column
     */
    public String parseFamilyName() {
        Element familyName = parent(familyNameSelector);
        return ownText(familyName);
    }

    /*
     * Parses the birthday from the 'Birthday' row
     * in the left column
     */
    public String parseBirthday() {
        Element birthday = parent(birthdaySelector);
        return ownText(birthday);
    }

    /*
     * Parses the website url from the 'Website' row
     * in the left column
     */
    public String parseWebsite() {
        Element website = nextElementSibling(websiteSelector);
        return html(website);
    }

    /*
     * Parses the number of members that have marked
     * the person as a favorite, from the 'Member Favorites' row in
     * the left column
     */
    public int parseMemberFavorites() {
        Element memberFavoritesElem = parent(memberFavoritesSelector);

        if (memberFavoritesElem == null) {
            return 0;
        }

        String favoritesText = memberFavoritesElem.ownText();
        favoritesText = favoritesText.replace(",", "");
        return Integer.parseInt(favoritesText);
    }

    /*
     * Parses additional information that does not fall
     * under the other categories from the 'More' row in
     * the left column
     */
    public String parseMore() {
        Element moreElem = doc.selectFirst(moreSelector);
        return html(moreElem);
    }
}