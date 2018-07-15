package hibernateUtils.daos;

import hibernateUtils.mappings.animes.Anime;
import hibernateUtils.mappings.joinTables.AnimeCharacter;
import hibernateUtils.mappings.joinTables.AnimeStaffRole;
import hibernateUtils.mappings.mangas.Manga;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class AnimeDao extends GenericDao {
    public Anime getAnime(int animeId) {
        return (Anime)getMalMapping(animeId, Anime.class);
    }

    public void addCharacterRoles(List<AnimeCharacter> animeCharacters) {
        for (AnimeCharacter animeChar : animeCharacters) {
            saveOrUpdateMalMapping(animeChar);
        }
    }

    public void addStaffRoles(List<AnimeStaffRole> animeStaffRoles) {
        for (AnimeStaffRole animeStaff : animeStaffRoles) {
            saveOrUpdateMalMapping(animeStaff);
        }
    }

    public void addMangaAdaptation(Anime anime, Manga manga) {
        Session session = factory.getCurrentSession();
        session.update(anime);

        //System.out.println(anime.getMainTitle() + "    " + manga.getMainTitle());

        //anime.getMangaAdaptations().add(manga);
        saveOrUpdateMalMapping(anime);
    }
}
