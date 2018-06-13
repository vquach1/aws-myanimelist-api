package hibernateUtils.hibernateObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "myanimelist.anime_statistics")
public class AnimeStatistic extends AnimeMangaStatistic {
    @Column(name = "watching")
    protected int watching;

    @Column(name = "plan_to_watch")
    protected int planToWatch;

    public AnimeStatistic() {}

    public int getWatching() {
        return watching;
    }

    public void setWatching(int watching) {
        this.watching = watching;
    }

    public int getPlanToWatch() {
        return planToWatch;
    }

    public void setPlanToWatch(int planToWatch) {
        this.planToWatch = planToWatch;
    }
}
