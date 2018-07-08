package hibernateUtils.hibernateMappings.lookupTableMappings;

import hibernateUtils.hibernateMappings.abstractMappings.LookupTable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.producer_types")
public class ProducerType extends LookupTable {
    public ProducerType() {}

    public ProducerType(ProducerType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
