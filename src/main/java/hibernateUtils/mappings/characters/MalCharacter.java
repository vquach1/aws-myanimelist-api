package hibernateUtils.mappings.characters;

import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.joinTables.AnimeCharacter;
import hibernateUtils.mappings.joinTables.VoiceActorRole;

import javax.persistence.*;
import java.util.Set;


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

    @OneToMany(mappedBy = "pk.malCharacter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<VoiceActorRole> voiceActorRoles;

    @OneToMany(mappedBy = "pk.malCharacter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<AnimeCharacter> animeCharacters;

    public MalCharacter() {}

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

    public Set<VoiceActorRole> getVoiceActorRoles() {
        return voiceActorRoles;
    }

    public void setVoiceActorRoles(Set<VoiceActorRole> voiceActorRoles) {
        this.voiceActorRoles = voiceActorRoles;
    }

    public Set<AnimeCharacter> getAnimeCharacters() {
        return animeCharacters;
    }

    public void setAnimeCharacters(Set<AnimeCharacter> animeCharacters) {
        this.animeCharacters = animeCharacters;
    }
}
