package pageTypes.mangas;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pageTypes.abstracts.AnimeAndMangaDetailsPage;

/*
 * Provides HTML scraping utilities for pages of the path '/manga/{mangaId}/{mangaName}'
 * Example: /manga/21/Death_Note
 */
public class MangaDetailsPage extends AnimeAndMangaDetailsPage {
    private static final String volumesSelector = "span[class='dark_text']:contains(Volumes:)";
    private static final String chaptersSelector = "span[class='dark_text']:contains(Chapters:)";
    private static final String publishedSelector = "span[class='dark_text']:contains(Published:)";
    private static final String serializationSelector = "span[class='dark_text']:contains(Serialization:)";

    public MangaDetailsPage(Document doc) {
        super(doc);
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
}
