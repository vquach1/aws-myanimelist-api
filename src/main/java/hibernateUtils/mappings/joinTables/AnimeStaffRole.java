package hibernateUtils.mappings.joinTables;

import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.animes.Anime;
import hibernateUtils.mappings.lookupTables.AnimeStaffRoleType;
import hibernateUtils.mappings.persons.Person;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "myanimelist.anime_staff_roles")
public class AnimeStaffRole extends MalMapping {
    @EmbeddedId
    private AnimeStaffRolePk pk;

    public AnimeStaffRole(Anime anime, Person person, AnimeStaffRoleType animeStaffRoleType) {
        pk = new AnimeStaffRolePk(anime, person, animeStaffRoleType);
    }

    @Transient
    public Anime getAnime() {
        return pk.getAnime();
    }

    @Transient
    public Person getPerson() {
        return pk.getPerson();
    }

    @Transient
    public AnimeStaffRoleType getAnimeStaffRoleType() {
        return pk.getAnimeStaffRoleType();
    }

    private static class AnimeStaffRolePk implements Serializable {
        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "anime_id")
        private Anime anime;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "person_id")
        private Person person;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "anime_staff_role_type_id")
        private AnimeStaffRoleType animeStaffRoleType;

        public AnimeStaffRolePk() {}

        public AnimeStaffRolePk(Anime anime, Person person, AnimeStaffRoleType animeStaffRoleType) {
            this.anime = anime;
            this.person = person;
            this.animeStaffRoleType = animeStaffRoleType;
        }

        public Anime getAnime() {
            return anime;
        }

        public void setAnime(Anime anime) {
            this.anime = anime;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public AnimeStaffRoleType getAnimeStaffRoleType() {
            return animeStaffRoleType;
        }

        public void setAnimeStaffRoleType(AnimeStaffRoleType animeStaffRoleType) {
            this.animeStaffRoleType = animeStaffRoleType;
        }
    }
}
