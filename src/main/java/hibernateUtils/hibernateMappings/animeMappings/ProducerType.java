package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.producer_types")
public class ProducerType extends PairMapping {
    public ProducerType() {}

    public ProducerType(ProducerType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
