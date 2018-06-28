package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_status_types")
public class AnimeStatusType extends PairMapping {
    public AnimeStatusType() {}

    public AnimeStatusType(AnimeStatusType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
