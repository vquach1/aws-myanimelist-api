package hibernateUtils.hibernateMappings.abstractMappings;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/*
 * This class is primarily for Hibernate mappings on tables
 * that only have 'id' and 'name' columns. Such mappings
 * are typically used to propagate HashMaps for the Converters,
 * and classifying these mappings in their own subtype eases
 * the propagation.
 */

@MappedSuperclass
public class LookupTable extends MalMapping {
    @Id
    @Column(name = "id")
    protected int id;

    @Column(name = "name")
    protected String name;

    public LookupTable() {}

    public LookupTable(LookupTable other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public LookupTable(int id, String name) {
        this.id = id;
        this.name = name.trim();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
