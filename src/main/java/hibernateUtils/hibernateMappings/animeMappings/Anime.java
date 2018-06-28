package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "myanimelist.anime")
public class Anime extends MalMapping {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "anime_type_id")
    private int animeTypeId;

    @Column(name = "anime_status_type_id")
    private int animeStatusTypeId;

    @Column(name = "anime_age_rating_type_id")
    private int animeAgeRatingTypeId;

    @Column(name = "anime_season_type_id")
    private int animeSeasonTypeId;

    @Column(name = "anime_source_type_id")
    private int animeSourceTypeId;

    @Column(name = "num_episodes")
    private int numEpisodes;

    @Column(name = "episode_duration")
    private int episodeDuration;

    @Column(name = "start_date")
    private Calendar startDate;

    @Column(name = "end_date")
    private Calendar endDate;

    @Column(name = "broadcast")
    private String broadcast;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "background")
    private String background;

    public Anime() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimeTypeId() {
        return animeTypeId;
    }

    public void setAnimeTypeId(int animeTypeId) {
        this.animeTypeId = animeTypeId;
    }

    public int getAnimeStatusTypeId() {
        return animeStatusTypeId;
    }

    public void setAnimeStatusTypeId(int animeStatusTypeId) {
        this.animeStatusTypeId = animeStatusTypeId;
    }

    public int getAnimeAgeRatingTypeId() {
        return animeAgeRatingTypeId;
    }

    public void setAnimeAgeRatingTypeId(int animeAgeRatingTypeId) {
        this.animeAgeRatingTypeId = animeAgeRatingTypeId;
    }

    public int getAnimeSeasonTypeId() {
        return animeSeasonTypeId;
    }

    public void setAnimeSeasonTypeId(int animeSeasonTypeId) {
        this.animeSeasonTypeId = animeSeasonTypeId;
    }

    public int getAnimeSourceTypeId() {
        return animeSourceTypeId;
    }

    public void setAnimeSourceTypeId(int animeSourceTypeId) {
        this.animeSourceTypeId = animeSourceTypeId;
    }

    public int getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(int numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    public int getEpisodeDuration() {
        return episodeDuration;
    }

    public void setEpisodeDuration(int episodeDuration) {
        this.episodeDuration = episodeDuration;
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

    public String getSynopsis() {
        return synopsis;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
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
}
