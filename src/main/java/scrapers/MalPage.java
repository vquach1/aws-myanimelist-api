package scrapers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.*;
import java.util.regex.Pattern;

abstract public class MalPage{
    protected Document doc;

    protected static String producerIdRegex = "/anime/producer/(\\d+)";
    protected static String animeIdRegex = "/anime/(\\d+)";
    protected static String mangaIdRegex = "/manga/(\\d+)";
    protected static String personIdRegex = "/people/(\\d+)";
    protected static String genreIdRegex = "/genre/(\\d+)";

    protected static Pattern producerIdPattern = Pattern.compile(producerIdRegex);
    protected static Pattern animeIdPattern = Pattern.compile(animeIdRegex);
    protected static Pattern mangaIdPattern = Pattern.compile(mangaIdRegex);
    protected static Pattern personIdPattern = Pattern.compile(personIdRegex);
    protected static Pattern genreIdPattern = Pattern.compile(genreIdRegex);

    public MalPage(Document doc) {
        this.doc = doc;
    }

    public Element parent(String selector) {
        Element match = doc.selectFirst(selector);
        return match == null ? null : match.parent();
    }

    public Element nextElementSibling(String selector) {
        Element match = doc.selectFirst(selector);

        if (match == null || match.nextElementSibling() == null) {
            return null;
        }

        return match.nextElementSibling();
    }

    public String ownText(Element elem) {
        return elem == null ? "" : elem.ownText();
    }

    public String html(Element elem) {
        return elem == null ? "" : elem.html();
    }

    public int parseNumber(String numberStr) {
        return Integer.parseInt(numberStr.replace(",", ""));
    }
}
