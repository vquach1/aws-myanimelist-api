package scrapers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.NumberFormat;

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

    public String parseName() {
        return StringUtils.removeEnd(doc.title(), " - MyAnimeList.net");
    }

    public String parseGivenName() {
        Element givenName = parent(givenNameSelector);
        return ownText(givenName);
    }

    public String parseFamilyName() {
        Element familyName = parent(familyNameSelector);
        return ownText(familyName);
    }

    public String parseBirthday() {
        Element birthday = parent(birthdaySelector);
        return ownText(birthday);
    }

    public String parseWebsite() {
        Element website = nextElementSibling(websiteSelector);
        return html(website);
    }

    public int parseMemberFavorites() {
        Element memberFavoritesElem = parent(memberFavoritesSelector);

        if (memberFavoritesElem == null) {
            return 0;
        }

        String favoritesText = memberFavoritesElem.ownText();
        favoritesText = favoritesText.replace(",", "");
        return Integer.parseInt(favoritesText);
    }

    public String parseMore() {
        Element moreElem = doc.selectFirst(moreSelector);
        return html(moreElem);
    }
}