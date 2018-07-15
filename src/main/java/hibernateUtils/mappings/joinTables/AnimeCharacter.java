package hibernateUtils.mappings.joinTables;


import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.animes.Anime;
import hibernateUtils.mappings.characters.MalCharacter;
import hibernateUtils.mappings.lookupTables.MalCharacterRoleType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "myanimelist.anime_characters")
public class AnimeCharacter extends MalMapping {
    @EmbeddedId
    private AnimeCharacterPk pk;

    public AnimeCharacter() {}

    public AnimeCharacter(Anime anime, MalCharacter malCharacter, MalCharacterRoleType malCharacterRoleType) {
        pk = new AnimeCharacterPk(anime, malCharacter, malCharacterRoleType);
    }

    @Transient
    public Anime getAnime() {
        return pk.getAnime();
    }

    @Transient
    public MalCharacter getMalCharacter() {
        return pk.getMalCharacter();
    }

    @Transient
    public MalCharacterRoleType getMalCharacterRoleType() {
        return pk.getMalCharacterRoleType();
    }

    private static class AnimeCharacterPk implements Serializable {
        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "anime_id")
        private Anime anime;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "character_id")
        private MalCharacter malCharacter;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "character_role_type_id")
        private MalCharacterRoleType malCharacterRoleType;

        public AnimeCharacterPk() {}

        public AnimeCharacterPk(Anime anime, MalCharacter malCharacter, MalCharacterRoleType malCharacterRoleType) {
            this.anime = anime;
            this.malCharacter = malCharacter;
            this.malCharacterRoleType = malCharacterRoleType;
        }

        public Anime getAnime() {
            return anime;
        }

        public void setAnime(Anime anime) {
            this.anime = anime;
        }

        public MalCharacter getMalCharacter() {
            return malCharacter;
        }

        public void setMalCharacter(MalCharacter malCharacter) {
            this.malCharacter = malCharacter;
        }

        public MalCharacterRoleType getMalCharacterRoleType() {
            return malCharacterRoleType;
        }

        public void setMalCharacterRoleType(MalCharacterRoleType malCharacterRoleType) {
            this.malCharacterRoleType = malCharacterRoleType;
        }
    }
}
