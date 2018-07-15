package hibernateUtils.mappings.persons;

import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.joinTables.MangaAuthor;
import hibernateUtils.mappings.joinTables.VoiceActorRole;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "myanimelist.people")
public class Person extends MalMapping {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "english_first_name")
    private String englishFirstName;

    @Column(name = "english_last_name")
    private String englishLastName;

    @Column(name = "foreign_first_name")
    private String foreignFirstName;

    @Column(name = "foreign_last_name")
    private String foreignLastName;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "website")
    private String website;

    @Column(name = "favorites")
    private int favorites;

    @Column(name = "other_info")
    private String otherInfo;

    @OneToMany(mappedBy = "pk.person", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<VoiceActorRole> voiceActorRoles;

    @OneToMany(mappedBy = "pk.person", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<MangaAuthor> mangaAuthors;

    public Person() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getForeignFirstName() {
        return foreignFirstName;
    }

    public void setForeignFirstName(String foreignFirstName) {
        this.foreignFirstName = foreignFirstName;
    }

    public String getForeignLastName() {
        return foreignLastName;
    }

    public void setForeignLastName(String foreignLastName) {
        this.foreignLastName = foreignLastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Set<VoiceActorRole> getVoiceActorRoles() {
        return voiceActorRoles;
    }

    public void setVoiceActorRoles(Set<VoiceActorRole> voiceActorRoles) {
        this.voiceActorRoles = voiceActorRoles;
    }

    public Set<MangaAuthor> getMangaAuthors() {
        return mangaAuthors;
    }

    public void setMangaAuthors(Set<MangaAuthor> mangaAuthors) {
        this.mangaAuthors = mangaAuthors;
    }
}
