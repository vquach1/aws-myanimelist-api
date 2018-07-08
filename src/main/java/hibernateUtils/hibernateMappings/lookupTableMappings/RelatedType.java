package hibernateUtils.hibernateMappings.lookupTableMappings;

import hibernateUtils.hibernateMappings.abstractMappings.LookupTable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.related_types")
public class RelatedType extends LookupTable {
    public RelatedType() {}

    public RelatedType(RelatedType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
