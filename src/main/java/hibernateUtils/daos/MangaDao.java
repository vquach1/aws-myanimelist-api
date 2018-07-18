package hibernateUtils.daos;

import hibernateUtils.mappings.animes.Anime;
import hibernateUtils.mappings.joinTables.MangaAuthor;
import hibernateUtils.mappings.joinTables.MangaRelated;
import hibernateUtils.mappings.mangas.Manga;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class MangaDao extends GenericDao {
    public Manga getManga(int mangaId) {
        return (Manga)getMalMapping(mangaId, Manga.class);
    }

    public void addMangaAuthors(List<MangaAuthor> mangaAuthors) {
        for (MangaAuthor mangaAuthor : mangaAuthors) {
            saveOrUpdateMalMapping(mangaAuthor);
        }
    }

    public void addMangaRelated(List<MangaRelated> relatedMangas) {
        for (MangaRelated related : relatedMangas) {
            saveOrUpdateMalMapping(related);
        }
    }
}
