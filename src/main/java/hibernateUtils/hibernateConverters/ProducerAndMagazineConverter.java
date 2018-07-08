package hibernateUtils.hibernateConverters;

import hibernateUtils.hibernateConverters.abstractConverters.Converter;
import hibernateUtils.hibernateMappings.mangaMappings.Magazine;
import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;
import hibernateUtils.hibernateMappings.animeMappings.Producer;
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

        hibernateUtils.saveOrUpdateMalMappings(idToProducerMap.values());
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

        hibernateUtils.saveOrUpdateMalMappings(idToMagazineMap.values());
    }

    public void convert() {
        convertProducers();
        convertMagazines();
    }
}
