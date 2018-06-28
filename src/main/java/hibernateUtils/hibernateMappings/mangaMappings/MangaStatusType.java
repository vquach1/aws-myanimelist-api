package hibernateUtils.hibernateMappings.mangaMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.manga_status_types")
public class MangaStatusType extends PairMapping {
    public MangaStatusType() {}

    public MangaStatusType(MangaStatusType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
