package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.Producer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrapers.ProducerPage;

import java.util.HashMap;

public class ProducerConverter extends Converter {
    public ProducerConverter(String bucketName) {
        super(bucketName);
    }

    public void convert(String key) {
        String rawHtml = s3Utils.readObject(bucketName, key);
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        ProducerPage page = new ProducerPage(doc);

        HashMap<Integer, String> res = page.parseProducers();

        for (Integer id : res.keySet()) {
            Producer producer = new Producer(id, res.get(id));
            hibernateUtils.updateMalMapping(id, producer);
        }
    }
}
