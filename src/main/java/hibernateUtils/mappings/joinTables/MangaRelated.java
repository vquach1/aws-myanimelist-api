package hibernateUtils.mappings.joinTables;

import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.lookupTables.RelatedType;
import hibernateUtils.mappings.mangas.Manga;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "myanimelist.manga_related")
public class MangaRelated extends MalMapping {
    @EmbeddedId
    private MangaRelatedPk pk;

    public MangaRelated(Manga mangaOne, Manga mangaTwo, RelatedType relatedType) {
        pk = new MangaRelatedPk(mangaOne, mangaTwo, relatedType);
    }

    @Transient
    public Manga getMangaOne() {
        return pk.getMangaOne();
    }

    @Transient
    public Manga getMangaTwo() {
        return pk.getMangaTwo();
    }

    @Transient
    public RelatedType getRelatedType() {
        return pk.getRelatedType();
    }

    private static class MangaRelatedPk implements Serializable {
        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "manga_id1")
        private Manga mangaOne;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "manga_id2")
        private Manga mangaTwo;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "related_type_id")
        private RelatedType relatedType;

        private MangaRelatedPk(Manga mangaOne, Manga mangaTwo, RelatedType relatedType) {
            this.mangaOne = mangaOne;
            this.mangaTwo = mangaTwo;
            this.relatedType = relatedType;
        }

        public Manga getMangaOne() {
            return mangaOne;
        }

        public void setMangaOne(Manga mangaOne) {
            this.mangaOne = mangaOne;
        }

        public Manga getMangaTwo() {
            return mangaTwo;
        }

        public void setMangaTwo(Manga mangaTwo) {
            this.mangaTwo = mangaTwo;
        }

        public RelatedType getRelatedType() {
            return relatedType;
        }

        public void setRelatedType(RelatedType relatedType) {
            this.relatedType = relatedType;
        }
    }
}
