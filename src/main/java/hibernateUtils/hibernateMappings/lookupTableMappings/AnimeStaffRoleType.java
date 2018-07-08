package hibernateUtils.hibernateMappings.lookupTableMappings;

import hibernateUtils.hibernateMappings.abstractMappings.LookupTable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "anime_staff_role_types")
public class AnimeStaffRoleType extends LookupTable {
    public AnimeStaffRoleType() {}

    public AnimeStaffRoleType(AnimeStaffRoleType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
