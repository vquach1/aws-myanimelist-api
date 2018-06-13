package hibernateUtils.hibernateObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_season_types")
public class AnimeSeasonType extends MalMapping {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public AnimeSeasonType() {}

    public AnimeSeasonType(AnimeSeasonType other) {
        this.id = other.id;
        this.name = other.name.trim();
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
