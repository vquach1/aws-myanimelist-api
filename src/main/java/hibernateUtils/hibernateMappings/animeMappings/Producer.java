package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;
import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.producers")
public class Producer extends PairMapping {
    public Producer() {}

    public Producer(Producer other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Producer(int id, String name) {
        super(id, name);
    }
}
