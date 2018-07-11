package hibernateUtils.daos;

import hibernateUtils.mappings.characters.MalCharacter;
import hibernateUtils.mappings.joinTables.VoiceActorRole;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class MalCharacterDao extends GenericDao {
    public MalCharacter getMalCharacter(int charId) {
        return (MalCharacter)getMalMapping(charId, MalCharacter.class);
    }

    public void addVoiceActors(List<VoiceActorRole> voiceActorRoles) {
        for (VoiceActorRole role : voiceActorRoles) {
            saveOrUpdateMalMapping(role);
        }
    }
}
