package scrapers.scraperDataTypes;

import java.util.HashMap;

public class AnimeCharacterInfo {
    private String name;
    private String role;
    private HashMap<String, String> voiceActorToLanguage;

    public AnimeCharacterInfo(String name, String role, HashMap<String, String> voiceActorToLanguage) {
        this.name = name;
        this.role = role;
        this.voiceActorToLanguage = voiceActorToLanguage;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public HashMap<String, String> getVoiceActorToLanguage() {
        return voiceActorToLanguage;
    }
}
