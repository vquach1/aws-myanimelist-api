package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateObjects.Magazine;
import hibernateUtils.hibernateObjects.MalMapping;
import hibernateUtils.hibernateObjects.Producer;
import org.jsoup.nodes.Document;
import scrapers.ProducerAndMagazinePage;

import java.util.HashMap;

public class ProducerAndMagazineConverter extends Converter {
    public ProducerAndMagazineConverter() {}

    public void convertProducers() {
        String path = "anime/producer";
        Document doc = parseHtml(path);
        ProducerAndMagazinePage page = new ProducerAndMagazinePage(doc);

        // Map each producer's id to their name
        HashMap<Integer, String> idToNameMap = page.parseProducers();
        HashMap<Integer, MalMapping> idToProducerMap = new HashMap<>();
        for (Integer id : idToNameMap.keySet()) {
            Producer producer = new Producer(id, idToNameMap.get(id));
            idToProducerMap.put(id, producer);
        }

        hibernateUtils.updateMalMappings(idToProducerMap);
    }

    public void convertMagazines() {
        String path = "manga/magazine";
        Document doc = parseHtml(path);
        ProducerAndMagazinePage page = new ProducerAndMagazinePage(doc);

        // Map each producer's id to their name
        HashMap<Integer, String> idToNameMap = page.parseMagazines();
        HashMap<Integer, MalMapping> idToMagazineMap = new HashMap<>();
        for (Integer id : idToNameMap.keySet()) {
            Magazine magazine = new Magazine(id, idToNameMap.get(id));
            idToMagazineMap.put(id, magazine);
        }

        hibernateUtils.updateMalMappings(idToMagazineMap);
    }

    public void convert() {
        convertProducers();
        convertMagazines();
    }
}
