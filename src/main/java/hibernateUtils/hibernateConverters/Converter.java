package hibernateUtils.hibernateConverters;

import utils.MiscUtils;
import utils.S3Utils;
import utils.Downloader;
import hibernateUtils.HibernateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

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
    public Document parseHtml(String path) {
        if (s3Utils.objectMissingOrOutdated(path)) {
            downloader.download(path);
        }

        String rawHtml = s3Utils.readObject(path);
        return Jsoup.parse(rawHtml, "UTF-8");
    }

    /*
     * TODO:
     * The problem with this requirement is that not all
     * pages are associated with a (anime, manga, character, person) id.
     * A possibility is to make two types of Converters
     */
    //public abstract void convert(int id);
}
