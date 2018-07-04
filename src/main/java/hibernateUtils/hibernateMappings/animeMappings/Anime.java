package hibernateUtils.hibernateMappings.animeMappings;

import hibernateUtils.hibernateMappings.GenreType;
import hibernateUtils.hibernateMappings.abstractMappings.MalMapping;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "myanimelist.anime")
public class Anime extends MalMapping {
    //#region Basic fields

    @Id
    @Column(name = "id")
    private int id;

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

    //endregion

    //region JoinColumns and JoinTables

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "myanimelist.anime_genres",
        joinColumns = { @JoinColumn(name = "anime_id") },
        inverseJoinColumns = { @JoinColumn(name = "genre_type_id") })
    private Set<GenreType> genreTypes = new HashSet<>();

    @JoinTable(name = "myanimelist.anime_producers",
        joinColumns = { @JoinColumn(name = "anime_id") },
        inverseJoinColumns = { @JoinColumn(name = "producer_type_id") })
    @MapKeyJoinColumn(name = "producer_id")
    @ElementCollection
    private Map<Producer, ProducerType> producerToRole = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "anime_type_id")
    private AnimeType animeType;

    @ManyToOne
    @JoinColumn(name = "anime_status_type_id")
    private AnimeStatusType animeStatusType;

    @ManyToOne
    @JoinColumn(name = "anime_age_rating_type_id")
    private AnimeAgeRatingType animeAgeRatingType;

    @ManyToOne
    @JoinColumn(name = "anime_season_type_id")
    private AnimeSeasonType animeSeasonType;

    @ManyToOne
    @JoinColumn(name = "anime_source_type_id")
    private AnimeSourceType animeSourceType;

    //endregion

    public Anime() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //region Table Getters and Setters

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

    //endregion

    //region JoinTable or JoinColumn Getters and Setters

    public AnimeType getAnimeType() {
        return animeType;
    }

    public void setAnimeType(AnimeType animeType) {
        this.animeType = animeType;
    }

    public Set<GenreType> getGenreTypes() {
        return genreTypes;
    }

    public void setGenreTypes(Set<GenreType> genreTypes) {
        this.genreTypes = genreTypes;
    }

    public Map<Producer, ProducerType> getProducerToRole() {
        return producerToRole;
    }

    public void setProducerToRole(Map<Producer, ProducerType> producerToRole) {
        this.producerToRole = producerToRole;
    }

    public AnimeStatusType getAnimeStatusType() {
        return animeStatusType;
    }

    public void setAnimeStatusType(AnimeStatusType animeStatusType) {
        this.animeStatusType = animeStatusType;
    }

    public AnimeAgeRatingType getAnimeAgeRatingType() {
        return animeAgeRatingType;
    }

    public void setAnimeAgeRatingType(AnimeAgeRatingType animeAgeRatingType) {
        this.animeAgeRatingType = animeAgeRatingType;
    }

    public AnimeSeasonType getAnimeSeasonType() {
        return animeSeasonType;
    }

    public void setAnimeSeasonType(AnimeSeasonType animeSeasonType) {
        this.animeSeasonType = animeSeasonType;
    }

    public AnimeSourceType getAnimeSourceType() {
        return animeSourceType;
    }

    public void setAnimeSourceType(AnimeSourceType animeSourceType) {
        this.animeSourceType = animeSourceType;
    }

    //endregion
}
