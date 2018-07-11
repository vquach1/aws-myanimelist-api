package hibernateUtils.mappings.mangas;

import hibernateUtils.mappings.abstracts.AnimeMangaStatistic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.manga_statistics")
public class MangaStatistic extends AnimeMangaStatistic {
    @Column(name = "reading")
    private int reading;

    @Column(name = "plan_to_read")
    private int planToRead;

    public MangaStatistic() {}

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

    public int getPlanToRead() {
        return planToRead;
    }

    public void setPlanToRead(int planToRead) {
        this.planToRead = planToRead;
    }
}
