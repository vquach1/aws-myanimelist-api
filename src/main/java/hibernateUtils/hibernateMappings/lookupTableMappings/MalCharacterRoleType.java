package hibernateUtils.hibernateMappings.lookupTableMappings;

import hibernateUtils.hibernateMappings.abstractMappings.LookupTable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.character_role_types")
public class MalCharacterRoleType extends LookupTable {
    public MalCharacterRoleType() {}

    public MalCharacterRoleType(MalCharacterRoleType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
