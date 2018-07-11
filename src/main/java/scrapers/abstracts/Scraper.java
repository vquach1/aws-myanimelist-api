package scrapers.abstracts;

import hibernateUtils.daos.GenericDao;
import hibernateUtils.mappings.abstracts.LookupTable;
import utils.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public abstract class Scraper {
    @Autowired
    protected GenericDao genericDao;

    @Autowired
    protected MiscUtils miscUtils;

    @Autowired
    protected S3Utils s3Utils;

    @Autowired
    protected Downloader downloader;

    public Scraper() {}

    /*
     * Returns a Document with parsed HTML from 'https://myanimelist.net/<path>'
     */
    protected Document parseHtml(String path) {
        if (s3Utils.objectMissingOrOutdated(path)) {
            downloader.download(path);
        }

        String rawHtml = s3Utils.readObject(path);
        return Jsoup.parse(rawHtml, "UTF-8");
    }

    /*
     * Propagates a HashMap with (Name, LookupTable)
     * pairs based on the values retrieved from the specified
     * LookupTable class.
     */
    protected <T extends LookupTable> void fillNameToElemMap(HashMap<String, T> map, Class classIn) {
        if (!LookupTable.class.isAssignableFrom(classIn)) {
            throw new IllegalArgumentException("The provided class must be a subtype of LookupTable");
        }

        List<LookupTable> pMaps = (List<LookupTable>) genericDao.getTableRows(classIn);
        for (LookupTable pMap : pMaps) {
            map.put(pMap.getName(), (T)pMap);
        }
    }

    /*
     * Propagates a HashMap with (Id, LookupTable)
     * pairs based on the values retrieved from the specified
     * LookupTable class.
     */
    protected <T extends LookupTable> void fillIdToElemMap(HashMap<Integer, T> map, Class classIn) {
        if (!LookupTable.class.isAssignableFrom(classIn)) {
            throw new IllegalArgumentException("The provided class must be a subtype of LookupTable");
        }

        List<LookupTable> pMaps = (List<LookupTable>) genericDao.getTableRows(classIn);
        for (LookupTable pMap : pMaps) {
            map.put(pMap.getId(), (T)pMap);
        }
    }

    /*
     * TODO:
     * The problem with this requirement is that not all
     * pages are associated with a (anime, manga, character, person) id.
     * A possibility is to make two types of Converters
     */
    //public abstract void convert(int id);
}
