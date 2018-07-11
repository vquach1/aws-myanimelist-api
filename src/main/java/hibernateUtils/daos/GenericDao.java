package hibernateUtils.daos;

import hibernateUtils.mappings.abstracts.MalMapping;
import org.hibernate.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.InstantiationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional
@Component
public class GenericDao {
    @Autowired
    protected SessionFactory factory;

    public void saveOrUpdateMalMappings(Collection<MalMapping> malMappings) {
        for (MalMapping map: malMappings) {
            saveOrUpdateMalMapping(map);
        }
    }

    public void saveOrUpdateMalMapping(MalMapping mapping) {
        Session session = factory.getCurrentSession();
        session.saveOrUpdate(mapping);
    }

    public MalMapping getMalMapping(int id, Class className) {
        if (!MalMapping.class.isAssignableFrom(className)) {
            return null;
        }

        Session session = factory.getCurrentSession();
        MalMapping mapping = (MalMapping)session.get(className, id);
        return mapping;
    }

    /*
     * Returns a list of all objects that are found in
     * the specified Hibernate table. For example, getTableRows(Anime.class)
     * will return all of the objects (rows) in the Anime table
     */
    public List getTableRows(Class classIn) {
        // Balk if the class is not a mapping for Hibernate
        if (!MalMapping.class.isAssignableFrom(classIn)) {
            return new ArrayList();
        }

        // Retrieve all the rows from the specified table in Hibernate
        List results = new ArrayList<>();
        Session session = factory.getCurrentSession();
        Query query = session.createQuery("from " + classIn.getSimpleName());
        List rows = query.list();

        // Convert each row into an object of the class's type
        try {
            Constructor ctor = classIn.getConstructor(classIn);
            for (Object obj : rows) {
                results.add(ctor.newInstance(classIn.cast(obj)));
            }
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return results;
    }
}
