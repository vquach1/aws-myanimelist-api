package scrapers.scraperDataTypes;

import java.util.List;

public class AnimeStaffInfo {
    private String name;
    private List<String> roles;

    public AnimeStaffInfo(String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return roles;
    }
}
