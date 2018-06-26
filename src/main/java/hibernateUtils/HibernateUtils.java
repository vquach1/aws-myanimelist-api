package hibernateUtils;

import hibernateUtils.hibernateObjects.*;
import org.hibernate.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Transactional
public class HibernateUtils {
    @Autowired
    private SessionFactory factory;

    public void updateMalMappings(HashMap<Integer, MalMapping> idToMalMappingMap) {
        for (Integer id : idToMalMappingMap.keySet()) {
            updateMalMapping(id, idToMalMappingMap.get(id));
        }
    }

    public void updateMalMapping(int id, MalMapping mapping) {
        Session session = factory.getCurrentSession();
        MalMapping oldMapping = getMalMapping(id, mapping.getClass());

        if (oldMapping == null) {
            addMalMapping(mapping);
        } else {
            oldMapping.update(mapping);
            session.update(oldMapping); // TODO: Find out whether this line is necessary
        }
    }

    public void addMalMapping(MalMapping mapping) {
        Session session = factory.getCurrentSession();
        session.save(mapping);
    }

    public MalMapping getMalMapping(int id, Class className) {
        if (!MalMapping.class.isAssignableFrom(className)) {
            return null;
        }

        Session session = factory.getCurrentSession();
        MalMapping mapping = (MalMapping)session.get(className, id);
        return mapping;
    }

    public List getRows(String queryStr) {
        Session session = factory.getCurrentSession();
        Query query = session.createQuery(queryStr);
        return query.list();
    }

    // TODO: Not comfortable with how this method is in this class.
    // Can this be moved to another place? Look up service vs DAO
    public List<AnimeType> getAnimeTypes() {
        List<AnimeType> types = (List<AnimeType>)getRows("from AnimeType");
        return types.stream().map(type -> new AnimeType(type)).collect(Collectors.toList());
    }

    public List<AnimeStatusType> getAnimeStatusTypes() {
        List<AnimeStatusType> types = (List<AnimeStatusType>)getRows("from AnimeStatusType");
        return types.stream().map(type -> new AnimeStatusType(type)).collect(Collectors.toList());
    }

    public List<AnimeAgeRatingType> getAnimeAgeRatingTypes() {
        List<AnimeAgeRatingType> types = (List<AnimeAgeRatingType>)getRows("from AnimeAgeRatingType");
        return types.stream().map(type -> new AnimeAgeRatingType(type)).collect(Collectors.toList());
    }

    public List<AnimeSeasonType> getAnimeSeasonTypes() {
        List<AnimeSeasonType> types = (List<AnimeSeasonType>)getRows("from AnimeSeasonType");
        return types.stream().map(type -> new AnimeSeasonType(type)).collect(Collectors.toList());
    }

    public List<AnimeSourceType> getAnimeSourceTypes() {
        List<AnimeSourceType> types = (List<AnimeSourceType>)getRows("from AnimeSourceType");
        return types.stream().map(type -> new AnimeSourceType(type)).collect(Collectors.toList());
    }
}
