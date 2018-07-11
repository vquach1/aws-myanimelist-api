package scrapers;

import scrapers.abstracts.Scraper;
import hibernateUtils.mappings.mangas.Magazine;
import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.animes.Producer;
import org.jsoup.nodes.Document;
import pageTypes.ProducerAndMagazinePage;

import java.util.HashMap;

public class ProducerAndMagazineScraper extends Scraper {
    public ProducerAndMagazineScraper() {}

    public void convertProducers() {
        String path = "anime/producer";
        downloader.download(path);
        Document doc = parseHtml(path);
        ProducerAndMagazinePage page = new ProducerAndMagazinePage(doc);

        // Map each producer's id to their name
        HashMap<Integer, String> idToNameMap = page.parseProducers();
        HashMap<Integer, MalMapping> idToProducerMap = new HashMap<>();
        for (Integer id : idToNameMap.keySet()) {
            Producer producer = new Producer(id, idToNameMap.get(id));
            idToProducerMap.put(id, producer);
        }

        genericDao.saveOrUpdateMalMappings(idToProducerMap.values());
    }

    public void convertMagazines() {
        String path = "manga/magazine";
        downloader.download(path);
        Document doc = parseHtml(path);
        ProducerAndMagazinePage page = new ProducerAndMagazinePage(doc);

        // Map each producer's id to their name
        HashMap<Integer, String> idToNameMap = page.parseMagazines();
        HashMap<Integer, MalMapping> idToMagazineMap = new HashMap<>();
        for (Integer id : idToNameMap.keySet()) {
            Magazine magazine = new Magazine(id, idToNameMap.get(id));
            idToMagazineMap.put(id, magazine);
        }

        genericDao.saveOrUpdateMalMappings(idToMagazineMap.values());
    }

    public void convert() {
        convertProducers();
        convertMagazines();
    }
}
