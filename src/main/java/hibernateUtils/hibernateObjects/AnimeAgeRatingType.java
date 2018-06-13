package hibernateUtils.hibernateObjects;

import javax.persistence.*;

@Entity
@Table(name = "myanimelist.anime_age_rating_types")
public class AnimeAgeRatingType extends MalMapping {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public AnimeAgeRatingType() {}

    public AnimeAgeRatingType(AnimeAgeRatingType other) {
        this.id = other.id;
        this.name = other.name.trim();
        this.description = other.description.trim();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
