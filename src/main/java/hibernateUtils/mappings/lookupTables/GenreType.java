package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;
import hibernateUtils.mappings.animes.Anime;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "myanimelist.genre_types")
public class GenreType extends LookupTable {
    @ManyToMany(mappedBy = "genreTypes")
    private Set<Anime> animes = new HashSet<>();

    public GenreType() {}

    public GenreType(GenreType other) {
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
