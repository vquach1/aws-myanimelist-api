package hibernateUtils.mappings.joinTables;

import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.characters.MalCharacter;
import hibernateUtils.mappings.lookupTables.LanguageType;
import hibernateUtils.mappings.persons.Person;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "myanimelist.voice_actor_roles")
public class VoiceActorRole extends MalMapping {
    @EmbeddedId
    private VoiceActorRolePk pk;

    public VoiceActorRole(Person person, MalCharacter malCharacter, LanguageType languageType) {
        pk = new VoiceActorRolePk(person, malCharacter, languageType);
    }

    @Transient
    public Person getPerson() {
        return pk.getPerson();
    }

    @Transient
    public MalCharacter getMalCharacter() {
        return pk.getMalCharacter();
    }

    @Transient
    public LanguageType getLanguageType() {
        return pk.getLanguageType();
    }

    private static class VoiceActorRolePk implements Serializable {
        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "person_id")
        private Person person;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "character_id")
        private MalCharacter malCharacter;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "language_type_id")
        private LanguageType languageType;

        public VoiceActorRolePk() {}

        public VoiceActorRolePk(Person person, MalCharacter malCharacter, LanguageType languageType) {
            this.person = person;
            this.malCharacter = malCharacter;
            this.languageType = languageType;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public MalCharacter getMalCharacter() {
            return malCharacter;
        }

        public void setMalCharacter(MalCharacter malCharacter) {
            this.malCharacter = malCharacter;
        }

        public LanguageType getLanguageType() {
            return languageType;
        }

        public void setLanguageType(LanguageType languageType) {
            this.languageType = languageType;
        }
    }
}
