package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.anime_status_types")
public class AnimeStatusType extends PairMapping {
    @OneToMany(mappedBy = "animeStatusType")
    private Set<Anime> animes;

    public AnimeStatusType() {}

    public AnimeStatusType(AnimeStatusType other) {
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
