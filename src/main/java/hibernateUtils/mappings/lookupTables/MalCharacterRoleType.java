package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;
import hibernateUtils.mappings.joinTables.AnimeCharacter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "myanimelist.character_role_types")
public class MalCharacterRoleType extends LookupTable {
    @OneToMany(mappedBy = "pk.malCharacterRoleType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<AnimeCharacter> animeCharacters;

    public MalCharacterRoleType() {}

    public MalCharacterRoleType(MalCharacterRoleType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Set<AnimeCharacter> getAnimeCharacters() {
        return animeCharacters;
    }

    public void setAnimeCharacters(Set<AnimeCharacter> animeCharacters) {
        this.animeCharacters = animeCharacters;
    }
}
