package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;
import hibernateUtils.mappings.joinTables.MangaAuthor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.manga_author_types")
public class MangaAuthorType extends LookupTable {
    @OneToMany(mappedBy = "mangaAuthorType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<MangaAuthor> mangaAuthors;

    public MangaAuthorType() {}

    public MangaAuthorType(MangaAuthorType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Set<MangaAuthor> getMangaAuthors() {
        return mangaAuthors;
    }

    public void setMangaAuthors(Set<MangaAuthor> mangaAuthors) {
        this.mangaAuthors = mangaAuthors;
    }
}
