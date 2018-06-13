package scrapers;

import org.jsoup.nodes.Document;

import java.util.HashMap;

public class MagazinePage extends MalPage {
    public MagazinePage(Document doc) {
        super(doc);
    }

    public HashMap<Integer, String> parseMagazines() {
        return null;
    }
}
