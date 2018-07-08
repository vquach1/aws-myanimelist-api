package hibernateUtils.hibernateConverters.animeConverters;

import hibernateUtils.hibernateConverters.abstractConverters.Converter;
import hibernateUtils.hibernateMappings.lookupTableMappings.AnimeStaffRoleType;
import hibernateUtils.hibernateMappings.lookupTableMappings.MalCharacterRoleType;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scrapers.animeScrapers.AnimeCharacterStaffPage;

import javax.annotation.PostConstruct;
import java.util.HashMap;

public class AnimeCharacterStaffConverter extends Converter {
    private HashMap<String, MalCharacterRoleType> characterRoleTypeMap = new HashMap<>();
    private HashMap<String, AnimeStaffRoleType> animeStaffRoleTypeHashMap = new HashMap<>();

    @Autowired
    @Qualifier("animeIdToPathMap")
    private HashMap<Integer, String> animeIdToPathMap;

    public AnimeCharacterStaffConverter() {}

    @PostConstruct
    private void initializeMaps() {
        fillNameToElemMap(characterRoleTypeMap, MalCharacterRoleType.class);
        fillNameToElemMap(animeStaffRoleTypeHashMap, AnimeStaffRoleType.class);
    }

    public void convert(int animeId) {
        String path = animeIdToPathMap.get(animeId) + "/characters";
        Document doc = parseHtml(path);
        AnimeCharacterStaffPage page = new AnimeCharacterStaffPage(doc);

        if (page.isEmptyPage()) {
            return;
        }
    }
}
