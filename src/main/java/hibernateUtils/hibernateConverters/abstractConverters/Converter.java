package hibernateUtils.hibernateConverters.abstractConverters;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;
import utils.MiscUtils;
import utils.S3Utils;
import utils.Downloader;
import utils.HibernateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public abstract class Converter {
    @Autowired
    protected HibernateUtils hibernateUtils;

    @Autowired
    protected MiscUtils miscUtils;

    @Autowired
    protected S3Utils s3Utils;

    @Autowired
    protected Downloader downloader;

    public Converter() {}

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
     * Propagates a HashMap with (String, Integer)
     * pairs based on the values retrieved from the specified
     * PairMapping class.
     */
    protected HashMap<String, Integer> fillNameToIdMap(Class classIn) {
        if (!PairMapping.class.isAssignableFrom(classIn)) {
            throw new IllegalArgumentException("The provided class must be a subtype of PairMapping");
        }

        HashMap<String, Integer> nameToIdMap = new HashMap<>();
        List<PairMapping> pMaps = (List<PairMapping>)hibernateUtils.getTableRows(classIn);
        for (PairMapping pMap: pMaps) {
            nameToIdMap.put(pMap.getName(), pMap.getId());
        }

        return nameToIdMap;
    }

    /*
     * TODO:
     * The problem with this requirement is that not all
     * pages are associated with a (anime, manga, character, person) id.
     * A possibility is to make two types of Converters
     */
    //public abstract void convert(int id);
}
