package hibernateUtils.hibernateMappings.mangaMappings;

import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "myanimelist.manga")
public class Manga extends MalMapping {
    //region Ordinary Fields

    @Id
    @Column(name = "id")
    private int id;

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

    @ManyToOne
    @JoinColumn(name = "manga_type_id")
    private MangaType mangaType;

    @ManyToOne
    @JoinColumn(name = "manga_status_type_id")
    private MangaStatusType mangaStatusType;

    @ManyToOne
    @JoinColumn(name = "magazine_id")
    private Magazine magazine;

    //endregion

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
