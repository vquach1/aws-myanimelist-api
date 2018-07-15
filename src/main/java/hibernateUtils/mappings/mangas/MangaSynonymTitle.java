package hibernateUtils.mappings.mangas;

import hibernateUtils.mappings.abstracts.MalMapping;

import javax.persistence.*;

@Entity
@Table(name = "myanimelist.manga_synonym_titles")
public class MangaSynonymTitle extends MalMapping {
    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public MangaSynonymTitle() {}

    public MangaSynonymTitle(Manga manga, String name) {
        this.manga = manga;
        this.name = name;
    }

    public Manga getManga() {
        return manga;
    }

    public void setManga(Manga manga) {
        this.manga = manga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
