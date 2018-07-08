package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.LookupTable;

import javax.persistence.*;

@Entity
@Table(name = "myanimelist.producers")
public class Producer extends LookupTable {
    public Producer() {}

    public Producer(Producer other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Producer(int id, String name) {
        super(id, name);
    }
}
