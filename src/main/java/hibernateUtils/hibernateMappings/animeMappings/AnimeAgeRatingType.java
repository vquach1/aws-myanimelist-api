package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.PairMapping;

import javax.persistence.*;

@Entity
@Table(name = "myanimelist.anime_age_rating_types")
public class AnimeAgeRatingType extends PairMapping {
    @Column(name = "description")
    private String description;

    public AnimeAgeRatingType() {}

    public AnimeAgeRatingType(AnimeAgeRatingType other) {
        super(other);
        this.description = other.description.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
