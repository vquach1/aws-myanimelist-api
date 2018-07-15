package hibernateUtils.mappings.joinTables;

import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.lookupTables.MangaAuthorType;
import hibernateUtils.mappings.mangas.Manga;
import hibernateUtils.mappings.persons.Person;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "myanimelist.manga_authors")
public class MangaAuthor extends MalMapping {
    @EmbeddedId
    private MangaAuthorPk pk;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "manga_author_type_id")
    private MangaAuthorType mangaAuthorType;

    public MangaAuthor(Manga manga, Person person, MangaAuthorType mangaAuthorType) {
        pk = new MangaAuthorPk(manga, person);
        this.mangaAuthorType = mangaAuthorType;
    }

    @Transient
    public Manga getManga() {
        return pk.getManga();
    }

    @Transient
    public Person getPerson() {
        return pk.getPerson();
    }

    public MangaAuthorType getMangaAuthorType() { return mangaAuthorType; }

    private static class MangaAuthorPk implements Serializable {
        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "manga_id")
        private Manga manga;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "person_id")
        private Person person;

        public MangaAuthorPk() {}

        public MangaAuthorPk(Manga manga, Person person) {
            this.manga = manga;
            this.person = person;
        }

        public Manga getManga() {
            return manga;
        }

        public void setManga(Manga manga) {
            this.manga = manga;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }
    }
}
