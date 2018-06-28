package hibernateUtils.hibernateMappings.mangaMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.manga_types")
public class MangaType extends PairMapping {
    public MangaType() {}

    public MangaType(MangaType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
