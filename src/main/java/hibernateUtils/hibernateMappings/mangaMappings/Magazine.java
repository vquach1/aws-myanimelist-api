package hibernateUtils.hibernateMappings.mangaMappings;

import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;
import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.magazines")
public class Magazine extends PairMapping {
    public Magazine() {}

    public Magazine(Magazine other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Magazine(int id, String name) {
        super(id, name);
    }
}
