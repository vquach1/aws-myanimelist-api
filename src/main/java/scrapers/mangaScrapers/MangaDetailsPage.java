package scrapers.mangaScrapers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import scrapers.abstractScrapers.AnimeAndMangaDetailsPage;
import scrapers.abstractScrapers.MalPage;

public class MangaDetailsPage extends AnimeAndMangaDetailsPage {
    private static final String volumesSelector = "span[class='dark_text']:contains(Volumes:)";
    private static final String chaptersSelector = "span[class='dark_text']:contains(Chapters:)";
    private static final String publishedSelector = "span[class='dark_text']:contains(Published:)";
    private static final String serializationSelector = "span[class='dark_text']:contains(Serialization:)";

    public MangaDetailsPage(Document doc) {
        super(doc);
    }

    public String parseVolumes() {
        Element volumes = parent(volumesSelector);
        return ownText(volumes);
    }

    public String parseChapters() {
        Element chapters = parent(chaptersSelector);
        return ownText(chapters);
    }

    public String parsePublished() {
        Element published = parent(publishedSelector);
        return ownText(published);
    }

    public String parseSerialization() {
        Element serialization = parent(serializationSelector);

        if (serialization.children().size() == 1) {
            return "None";
        } else {
            return serialization.child(1).ownText();
        }
    }
}
