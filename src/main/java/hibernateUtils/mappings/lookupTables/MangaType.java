package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;
import hibernateUtils.mappings.mangas.Manga;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.manga_types")
public class MangaType extends LookupTable {
    @OneToMany(mappedBy = "mangaType")
    private Set<Manga> mangas;

    public MangaType() {}

    public MangaType(MangaType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Set<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(Set<Manga> mangas) {
        this.mangas = mangas;
    }
}
