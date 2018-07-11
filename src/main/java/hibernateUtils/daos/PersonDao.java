package hibernateUtils.daos;

import hibernateUtils.mappings.persons.Person;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class PersonDao extends GenericDao {
    public Person getPerson(int personId) {
        return (Person)getMalMapping(personId, Person.class);
    }
}
