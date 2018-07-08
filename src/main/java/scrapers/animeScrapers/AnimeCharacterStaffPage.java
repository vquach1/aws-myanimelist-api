package scrapers.animeScrapers;

import org.jsoup.nodes.Document;
import scrapers.abstractScrapers.MalPage;
import scrapers.scraperDataTypes.AnimeCharacterInfo;
import scrapers.scraperDataTypes.AnimeStaffInfo;

import java.util.ArrayList;
import java.util.List;

public class AnimeCharacterStaffPage extends MalPage {

    public AnimeCharacterStaffPage(Document doc) {
        super(doc);
    }

    public List<AnimeCharacterInfo> parseCharacters() {
        List<AnimeCharacterInfo> infos = new ArrayList<>();
        return infos;
    }

    public List<AnimeStaffInfo> parseStaff() {
        List<AnimeStaffInfo> infos = new ArrayList<>();
        return infos;
    }
}
