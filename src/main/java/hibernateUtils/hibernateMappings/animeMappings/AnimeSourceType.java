package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.anime_source_types")
public class AnimeSourceType extends PairMapping {
    @OneToMany(mappedBy = "animeSourceType")
    private Set<Anime> animes;

    public AnimeSourceType() {}

    public AnimeSourceType(AnimeSourceType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Set<Anime> getAnimes() {
        return animes;
    }

    public void setAnimes(Set<Anime> animes) {
        this.animes = animes;
    }
}
