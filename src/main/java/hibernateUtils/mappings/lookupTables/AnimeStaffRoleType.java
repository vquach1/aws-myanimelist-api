package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_staff_role_types")
public class AnimeStaffRoleType extends LookupTable {
    public AnimeStaffRoleType() {}

    public AnimeStaffRoleType(AnimeStaffRoleType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
