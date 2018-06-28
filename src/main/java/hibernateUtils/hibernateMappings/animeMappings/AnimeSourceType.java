package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_source_types")
public class AnimeSourceType extends PairMapping {
    public AnimeSourceType() {}

    public AnimeSourceType(AnimeSourceType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
