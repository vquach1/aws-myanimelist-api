package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;
import hibernateUtils.mappings.joinTables.MangaRelated;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.related_types")
public class RelatedType extends LookupTable {
    @OneToMany(mappedBy = "pk.relatedType")
    private Set<MangaRelated> mangaRelated;

    public RelatedType() {}

    public RelatedType(RelatedType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Set<MangaRelated> getMangaRelated() {
        return mangaRelated;
    }

    public void setMangaRelated(Set<MangaRelated> mangaRelated) {
        this.mangaRelated = mangaRelated;
    }
}
