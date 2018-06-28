package hibernateUtils.hibernateMappings.characterMappings;

import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "myanimelist.characters")
public class MalCharacter extends MalMapping {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "favorites")
    private int favorites;

    @Column(name = "english_first_name")
    private String englishFirstName;

    @Column(name = "english_last_name")
    private String englishLastName;

    @Column(name = "foreign_names")
    private String foreignNames;

    @Column(name = "biography")
    private String biography;

    public MalCharacter() {}

    public MalCharacter(int id, int favorites, String englishFirstName, String englishLastName,
                        String foreignNames, String biography) {
        this.id = id;
        this.favorites = favorites;
        this.englishFirstName = englishFirstName;
        this.englishLastName = englishLastName;
        this.foreignNames = foreignNames;
        this.biography = biography;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getEnglishFirstName() {
        return englishFirstName;
    }

    public void setEnglishFirstName(String englishFirstName) {
        this.englishFirstName = englishFirstName;
    }

    public String getEnglishLastName() {
        return englishLastName;
    }

    public void setEnglishLastName(String englishLastName) {
        this.englishLastName = englishLastName;
    }

    public String getForeignNames() {
        return foreignNames;
    }

    public void setForeignNames(String foreignNames) {
        this.foreignNames = foreignNames;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
