package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_season_types")
public class AnimeSeasonType extends PairMapping {
    public AnimeSeasonType() {}

    public AnimeSeasonType(AnimeSeasonType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }
}
