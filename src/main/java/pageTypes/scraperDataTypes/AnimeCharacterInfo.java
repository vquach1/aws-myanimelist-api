package pageTypes.scraperDataTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Encapsulates the information from a single character row in
 * the 'Characters & Voice Actors' table of any page with path
 * /anime/{animeId}/{animeName}/characters
 *
 * Used an an auxiliary data structure for an AnimeCharacterStaff page
 */
public class AnimeCharacterInfo {
    /*
     * Path to the character's details page
     */
    private String characterPath;

    /*
     * The character's role in the anime. Either 'Main' or 'Supporting'
     */
    private String characterRole;

    /*
     * A map encapsulating all of the character's voice actors, where...
     * -Key: Path of the voice actor's details page
     * -Value: The language that they voice acted in
     */
    private Map<String, String> voiceActorPathToLanguage;

    public AnimeCharacterInfo(String characterPath, String role, Map<String, String> voiceActorPathToLanguage) {
        this.characterPath = characterPath;
        this.characterRole = role;
        this.voiceActorPathToLanguage = voiceActorPathToLanguage;
    }

    public String getCharacterPath() {
        return characterPath;
    }

    public String getCharacterRole() {
        return characterRole;
    }

    public Map<String, String> getVoiceActorPathToLanguage() {
        return voiceActorPathToLanguage;
    }

    public List<String> getVoiceActorPaths() {
        return new ArrayList<>(voiceActorPathToLanguage.keySet());
    }
}
