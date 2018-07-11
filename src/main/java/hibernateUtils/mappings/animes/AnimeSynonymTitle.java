package hibernateUtils.mappings.animes;

import hibernateUtils.mappings.abstracts.MalMapping;

import javax.persistence.*;

@Entity
@Table(name = "myanimelist.anime_synonym_titles")
public class AnimeSynonymTitle extends MalMapping {
    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public AnimeSynonymTitle() {}

    public AnimeSynonymTitle(Anime anime, String name) {
        this.anime = anime;
        this.name = name;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
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
