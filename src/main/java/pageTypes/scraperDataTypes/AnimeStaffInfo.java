package pageTypes.scraperDataTypes;

import java.util.List;

/*
 * Encapsulates the information from a single person
 * row in the 'Staff' table of any page with path
 * /anime/{animeId}/{animeName}/characters
 *
 * Used an an auxiliary data structure for an AnimeCharacterStaff page
 */
public class AnimeStaffInfo {
    /*
     * Path to the person's details page
     */
    private String personPath;

    /*
     * List of all the person's jobs for the show. Examples
     * include 'Script, 'Editing', or 'Color Design'
     */
    private List<String> roles;

    public AnimeStaffInfo(String personPath, List<String> roles) {
        this.personPath = personPath;
        this.roles = roles;
    }

    public String getPersonPath() {
        return personPath;
    }

    public List<String> getRoles() {
        return roles;
    }
}
