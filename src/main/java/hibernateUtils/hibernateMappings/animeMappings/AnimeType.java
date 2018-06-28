package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_types")
public class AnimeType extends PairMapping {
    public AnimeType() {}

    public AnimeType(AnimeType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
