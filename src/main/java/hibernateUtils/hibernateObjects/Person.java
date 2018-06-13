package hibernateUtils.hibernateObjects;

import javax.persistence.*;

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

    public Person() {}

    public Person(int id, String englishFirstName, String englishLastName, String foreignFirstName,
                  String foreignLastName, String birthday, String website, int favorites, String otherInfo) {
        this.id = id;
        this.englishFirstName = englishFirstName;
        this.englishLastName = englishLastName;
        this.foreignFirstName = foreignFirstName;
        this.foreignLastName = foreignLastName;
        this.birthday = birthday;
        this.website = website;
        this.favorites = favorites;
        this.otherInfo = otherInfo;
    }

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
}
