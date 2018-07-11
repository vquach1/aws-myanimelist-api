package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;
import hibernateUtils.mappings.animes.Anime;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "myanimelist.anime_age_rating_types")
public class AnimeAgeRatingType extends LookupTable {
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "animeAgeRatingType")
    private Set<Anime> animes;

    public AnimeAgeRatingType() {}

    public AnimeAgeRatingType(AnimeAgeRatingType other) {
        super(other);
        this.description = other.description.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Anime> getAnimes() {
        return animes;
    }

    public void setAnimes(Set<Anime> animes) {
        this.animes = animes;
    }
}
