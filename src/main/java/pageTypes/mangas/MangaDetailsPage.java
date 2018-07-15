package pageTypes.mangas;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pageTypes.abstracts.AnimeAndMangaDetailsPage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Provides HTML scraping utilities for pages of the path '/manga/{mangaId}/{mangaName}'
 * Example: /manga/21/Death_Note
 */
public class MangaDetailsPage extends AnimeAndMangaDetailsPage {
    private static final String volumesSelector = "span[class='dark_text']:contains(Volumes:)";
    private static final String chaptersSelector = "span[class='dark_text']:contains(Chapters:)";
    private static final String publishedSelector = "span[class='dark_text']:contains(Published:)";
    private static final String serializationSelector = "span[class='dark_text']:contains(Serialization:)";
    private static final String authorSelector = "span[class='dark_text']:contains(Authors:)";

    public MangaDetailsPage(Document doc) {
        super(doc);
    }

    public String parseMainTitle() {
        return super.parseMainTitle().split(" | ")[0];
    }

    /*
     * Returns the number of volumes from the
     * 'Volumes' row in the 'Information' section
     */
    public String parseVolumes() {
        Element volumes = parent(volumesSelector);
        return ownText(volumes);
    }

    /*
     * Returns the number of chapters from the
     * 'Chapters' row in the 'Information' section
     */
    public String parseChapters() {
        Element chapters = parent(chaptersSelector);
        return ownText(chapters);
    }

    /*
     * Returns the start and end dates from the
     * 'Published' row in the 'Information' section
     */
    public String parsePublished() {
        Element published = parent(publishedSelector);
        return ownText(published);
    }

    /*
     * Returns the path of the magazine that serializes the manga.
     * Pulled from the 'Serialization' row in the 'Information' section
     */
    public String parseSerialization() {
        Element serialization = parent(serializationSelector);

        if (serialization.children().size() == 1) {
            return "None";
        } else {
            return serialization.child(1).ownText();
        }
    }

    public Map<String, String> parseAuthors() {
        Map<String, String> authorRoles = new HashMap<>();
        Element authors = parent(authorSelector);
        String[] roles = authors.ownText().split(",");

        /*
        System.out.println("Authors");
        for (int i = 1; i < authors.children().size(); i++) {
            System.out.println(authors.child(i));
        }

        System.out.println("Roles");
        for (String role : roles) {
            System.out.println(role);
        }
        */

        for (int i = 1; i < authors.children().size(); i++) {
            String personPath = authors.child(i).attr("href").substring(1);
            String role = roles[i-1].trim();
            role = role.substring(1, role.length()-1);

            authorRoles.put(personPath, role);
        }

        return authorRoles;
    }
}
