package hibernateUtils.hibernateObjects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AnimeMangaStatistic extends MalMapping {
    @Id
    @Column(name = "id")
    protected int id;

    @Column(name = "rated_one")
    protected int ratedOne;

    @Column(name = "rated_two")
    protected int ratedTwo;

    @Column(name = "rated_three")
    protected int ratedThree;

    @Column(name = "rated_four")
    protected int ratedFour;

    @Column(name = "rated_five")
    protected int ratedFive;

    @Column(name = "rated_six")
    protected int ratedSix;

    @Column(name = "rated_seven")
    protected int ratedSeven;

    @Column(name = "rated_eight")
    protected int ratedEight;

    @Column(name = "rated_nine")
    protected int ratedNine;

    @Column(name = "rated_ten")
    protected int ratedTen;

    @Column(name = "rated_one_percent")
    protected float ratedOnePercent;

    @Column(name = "rated_two_percent")
    protected float ratedTwoPercent;

    @Column(name = "rated_three_percent")
    protected float ratedThreePercent;

    @Column(name = "rated_four_percent")
    protected float ratedFourPercent;

    @Column(name = "rated_five_percent")
    protected float ratedFivePercent;

    @Column(name = "rated_six_percent")
    protected float ratedSixPercent;

    @Column(name = "rated_seven_percent")
    protected float ratedSevenPercent;

    @Column(name = "rated_eight_percent")
    protected float ratedEightPercent;

    @Column(name = "rated_nine_percent")
    protected float ratedNinePercent;

    @Column(name = "rated_ten_percent")
    protected float ratedTenPercent;

    @Column(name = "score")
    protected float score;

    @Column(name = "rank")
    protected int rank;

    @Column(name = "popularity")
    protected int popularity;

    @Column(name = "favorites")
    protected int favorites;

    @Column(name = "total")
    protected int total;

    @Column(name = "completed")
    protected int completed;

    @Column(name = "on_hold")
    protected int onHold;

    @Column(name = "dropped")
    protected int dropped;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRatedOne() {
        return ratedOne;
    }

    public void setRatedOne(int ratedOne) {
        this.ratedOne = ratedOne;
    }

    public int getRatedTwo() {
        return ratedTwo;
    }

    public void setRatedTwo(int ratedTwo) {
        this.ratedTwo = ratedTwo;
    }

    public int getRatedThree() {
        return ratedThree;
    }

    public void setRatedThree(int ratedThree) {
        this.ratedThree = ratedThree;
    }

    public int getRatedFour() {
        return ratedFour;
    }

    public void setRatedFour(int ratedFour) {
        this.ratedFour = ratedFour;
    }

    public int getRatedFive() {
        return ratedFive;
    }

    public void setRatedFive(int ratedFive) {
        this.ratedFive = ratedFive;
    }

    public int getRatedSix() {
        return ratedSix;
    }

    public void setRatedSix(int ratedSix) {
        this.ratedSix = ratedSix;
    }

    public int getRatedSeven() {
        return ratedSeven;
    }

    public void setRatedSeven(int ratedSeven) {
        this.ratedSeven = ratedSeven;
    }

    public int getRatedEight() {
        return ratedEight;
    }

    public void setRatedEight(int ratedEight) {
        this.ratedEight = ratedEight;
    }

    public int getRatedNine() {
        return ratedNine;
    }

    public void setRatedNine(int ratedNine) {
        this.ratedNine = ratedNine;
    }

    public int getRatedTen() {
        return ratedTen;
    }

    public void setRatedTen(int ratedTen) {
        this.ratedTen = ratedTen;
    }

    public float getRatedOnePercent() {
        return ratedOnePercent;
    }

    public void setRatedOnePercent(float ratedOnePercent) {
        this.ratedOnePercent = ratedOnePercent;
    }

    public float getRatedTwoPercent() {
        return ratedTwoPercent;
    }

    public void setRatedTwoPercent(float ratedTwoPercent) {
        this.ratedTwoPercent = ratedTwoPercent;
    }

    public float getRatedThreePercent() {
        return ratedThreePercent;
    }

    public void setRatedThreePercent(float ratedThreePercent) {
        this.ratedThreePercent = ratedThreePercent;
    }

    public float getRatedFourPercent() {
        return ratedFourPercent;
    }

    public void setRatedFourPercent(float ratedFourPercent) {
        this.ratedFourPercent = ratedFourPercent;
    }

    public float getRatedFivePercent() {
        return ratedFivePercent;
    }

    public void setRatedFivePercent(float ratedFivePercent) {
        this.ratedFivePercent = ratedFivePercent;
    }

    public float getRatedSixPercent() {
        return ratedSixPercent;
    }

    public void setRatedSixPercent(float ratedSixPercent) {
        this.ratedSixPercent = ratedSixPercent;
    }

    public float getRatedSevenPercent() {
        return ratedSevenPercent;
    }

    public void setRatedSevenPercent(float ratedSevenPercent) {
        this.ratedSevenPercent = ratedSevenPercent;
    }

    public float getRatedEightPercent() {
        return ratedEightPercent;
    }

    public void setRatedEightPercent(float ratedEightPercent) {
        this.ratedEightPercent = ratedEightPercent;
    }

    public float getRatedNinePercent() {
        return ratedNinePercent;
    }

    public void setRatedNinePercent(float ratedNinePercent) {
        this.ratedNinePercent = ratedNinePercent;
    }

    public float getRatedTenPercent() {
        return ratedTenPercent;
    }

    public void setRatedTenPercent(float ratedTenPercent) {
        this.ratedTenPercent = ratedTenPercent;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getOnHold() {
        return onHold;
    }

    public void setOnHold(int onHold) {
        this.onHold = onHold;
    }

    public int getDropped() {
        return dropped;
    }

    public void setDropped(int dropped) {
        this.dropped = dropped;
    }
}
