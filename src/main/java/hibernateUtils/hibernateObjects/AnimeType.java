package hibernateUtils.hibernateObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_types")
public class AnimeType extends MalMapping {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public AnimeType() {}

    public AnimeType(AnimeType other) {
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
