package hibernateUtils.mappings.mangas;

import hibernateUtils.mappings.abstracts.MalMapping;
import hibernateUtils.mappings.animes.Anime;
import hibernateUtils.mappings.joinTables.MangaAuthor;
import hibernateUtils.mappings.joinTables.MangaRelated;
import hibernateUtils.mappings.lookupTables.GenreType;
import hibernateUtils.mappings.lookupTables.MangaStatusType;
import hibernateUtils.mappings.lookupTables.MangaType;
import hibernateUtils.mappings.lookupTables.RelatedType;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "myanimelist.manga")
public class Manga extends MalMapping {
    //region Ordinary Fields

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "main_title")
    private String mainTitle;

    @Column(name = "english_title")
    private String englishTitle;

    @Column(name = "japanese_title")
    private String japaneseTitle;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "background")
    private String background;

    @Column(name = "volumes")
    private int volumes;

    @Column(name = "chapters")
    private int chapters;

    @Column(name = "start_date")
    private Calendar startDate;

    @Column(name = "end_date")
    private Calendar endDate;

    //endregion

    //region JoinColumns and JoinTables

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "myanimelist.manga_genres",
            joinColumns = { @JoinColumn(name = "manga_id") },
            inverseJoinColumns = { @JoinColumn(name = "genre_type_id") })
    private Set<GenreType> genreTypes = new HashSet<>();

    @ManyToMany(mappedBy = "mangaAdaptations")
    private Set<Anime> animeAdaptations = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "manga_type_id")
    private MangaType mangaType;

    @ManyToOne
    @JoinColumn(name = "manga_status_type_id")
    private MangaStatusType mangaStatusType;

    @ManyToOne
    @JoinColumn(name = "magazine_id")
    private Magazine magazine;

    @OneToMany(mappedBy = "manga")
    private Set<MangaSynonymTitle> mangaSynonymTitles;

    @OneToMany(mappedBy = "pk.manga", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<MangaAuthor> mangaAuthors;

    @OneToMany(mappedBy = "pk.mangaOne", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<MangaRelated> mangaRelated;

    //endregion

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }

    public String getJapaneseTitle() {
        return japaneseTitle;
    }

    public void setJapaneseTitle(String japaneseTitle) {
        this.japaneseTitle = japaneseTitle;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getVolumes() {
        return volumes;
    }

    public void setVolumes(int volumes) {
        this.volumes = volumes;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public MangaType getMangaType() {
        return mangaType;
    }

    public void setMangaType(MangaType mangaType) {
        this.mangaType = mangaType;
    }

    public MangaStatusType getMangaStatusType() {
        return mangaStatusType;
    }

    public void setMangaStatusType(MangaStatusType mangaStatusType) {
        this.mangaStatusType = mangaStatusType;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    public Set<Anime> getAnimeAdaptations() {
        return animeAdaptations;
    }

    public void setAnimeAdaptations(Set<Anime> animeAdaptations) {
        this.animeAdaptations = animeAdaptations;
    }

    public Set<MangaRelated> getMangaRelated() {
        return mangaRelated;
    }

    public void setMangaRelated(Set<MangaRelated> mangaRelated) {
        this.mangaRelated = mangaRelated;
    }

    public Set<GenreType> getGenreTypes() {
        return genreTypes;
    }

    public void setGenreTypes(Set<GenreType> genreTypes) {
        this.genreTypes = genreTypes;
    }

    public Set<MangaSynonymTitle> getMangaSynonymTitles() {
        return mangaSynonymTitles;
    }

    public void setMangaSynonymTitles(Set<MangaSynonymTitle> mangaSynonymTitles) {
        this.mangaSynonymTitles = mangaSynonymTitles;
    }

    public Set<MangaAuthor> getMangaAuthors() {
        return mangaAuthors;
    }

    public void setMangaAuthors(Set<MangaAuthor> mangaAuthors) {
        this.mangaAuthors = mangaAuthors;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Manga other = (Manga)obj;
        return id == other.id;
    }
}
