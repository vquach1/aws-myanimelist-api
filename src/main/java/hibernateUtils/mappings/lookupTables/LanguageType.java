package hibernateUtils.mappings.lookupTables;

import hibernateUtils.mappings.abstracts.LookupTable;
import hibernateUtils.mappings.joinTables.VoiceActorRole;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "myanimelist.language_types")
public class LanguageType extends LookupTable {
    @OneToMany(mappedBy = "pk.languageType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<VoiceActorRole> voiceActorRoles;

    public LanguageType() {}

    public LanguageType(LanguageType other) {
        this.id = other.id;
        this.name = other.name.trim();
    }

    public Set<VoiceActorRole> getVoiceActorRoles() {
        return voiceActorRoles;
    }

    public void setVoiceActorRoles(Set<VoiceActorRole> voiceActorRoles) {
        this.voiceActorRoles = voiceActorRoles;
    }
}
