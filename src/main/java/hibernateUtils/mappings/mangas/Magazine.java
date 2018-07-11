package hibernateUtils.mappings.mangas;

import hibernateUtils.mappings.abstracts.LookupTable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "myanimelist.magazines")
public class Magazine extends LookupTable {
    @OneToMany(mappedBy = "magazine")
    private Set<Manga> mangas;

    public Magazine() {}

    public Magazine(Magazine other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Magazine(int id, String name) {
        super(id, name);
    }

    public Set<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(Set<Manga> mangas) {
        this.mangas = mangas;
    }
}
