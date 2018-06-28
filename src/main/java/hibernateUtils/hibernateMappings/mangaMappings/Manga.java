package hibernateUtils.hibernateMappings.mangaMappings;

import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "myanimelist.manga")
public class Manga extends MalMapping {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "manga_type_id")
    private int mangaTypeId;

    @Column(name = "manga_status_type_id")
    private int mangaStatusTypeId;

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

    @Column(name = "magazine_id")
    private int magazineId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMangaTypeId() {
        return mangaTypeId;
    }

    public void setMangaTypeId(int mangaTypeId) {
        this.mangaTypeId = mangaTypeId;
    }

    public int getMangaStatusTypeId() {
        return mangaStatusTypeId;
    }

    public void setMangaStatusTypeId(int mangaStatusTypeId) {
        this.mangaStatusTypeId = mangaStatusTypeId;
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

    public int getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(int magazineId) {
        this.magazineId = magazineId;
    }
}
