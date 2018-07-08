package hibernateUtils.hibernateMappings.lookupTableMappings;

import hibernateUtils.hibernateMappings.abstractMappings.LookupTable;
import hibernateUtils.hibernateMappings.animeMappings.Anime;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.anime_types")
public class AnimeType extends LookupTable {
    @OneToMany(mappedBy = "animeType")
    private Set<Anime> animes;

    public AnimeType() {}

    public AnimeType(AnimeType other) {
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
