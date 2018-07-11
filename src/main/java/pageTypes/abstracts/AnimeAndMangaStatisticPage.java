package pageTypes.abstracts;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Provides HTML scraping utilities for pages of the path 'media/{mediaId}/{mediaTitle}/stats'
 * where media is either 'anime' or 'manga'
 * Example: anime/1735/Naruto__Shippuuden/stats
 */
public abstract class AnimeAndMangaStatisticPage extends MalPage {
    protected static final String scoreSelector = "span[itemprop='ratingValue']";
    protected static final String rankedSelector = "span[class='dark_text']:contains(Ranked:)";
    protected static final String popularitySelector = "span[class='dark_text']:contains(Popularity:)";
    protected static final String favoritesSelector = "span[class='dark_text']:contains(Favorites:)";
    protected static final String completedSelector = "span[class='dark_text']:contains(Completed:)";
    protected static final String onHoldSelector = "span[class='dark_text']:contains(On-Hold:)";
    protected static final String droppedSelector = "span[class='dark_text']:contains(Dropped:)";
    protected static final String totalSelector = "span[class='dark_text']:contains(Total:)";
    protected static final String scoreRatingSelector = "h2:contains(Score Stats)";

    protected static String ratingRegex = "((\\d|\\.)+)% <small>\\((\\d+) votes\\)</small>";
    protected static Pattern ratingPattern = Pattern.compile(ratingRegex);

    public AnimeAndMangaStatisticPage(Document doc) {
        super(doc);
    }

    /*
     * Returns the score for the media from the
     * 'Score' row in the 'Statistics section'
     */
    public float parseScore() {
        Element score = doc.selectFirst(scoreSelector);
        return score == null || score.ownText().equals("N/A") ? 0 : Float.parseFloat(score.ownText());
    }

    /*
     * Returns the score ranking for the media from the
     * 'Ranked' row in the 'Statistics section'
     */
    public int parseRanked() {
        Element ranked = parent(rankedSelector);

        if (ranked == null || ranked.ownText().equals("N/A")) {
            return 0;
        }

        String rankedStr = StringUtils.removeStart(ranked.ownText(), "#");
        return Integer.parseInt(rankedStr);
    }

    /*
     * Returns the popularity ranking for the media from the
     * 'Popularity' row in the 'Statistics section'
     */
    public int parsePopularity() {
        Element popularity = parent(popularitySelector);

        if (popularity == null) {
            return 0;
        }

        String popularityStr = StringUtils.removeStart(popularity.ownText(), "#");
        return Integer.parseInt(popularityStr);
    }

    /*
     * Returns the number of people that have favorited the media.
     * Pulls from 'Favorites' row of the 'Statistics' section
     */
    public int parseFavorites() {
        Element favorites = parent(favoritesSelector);
        return favorites == null ? 0 : parseNumber(favorites.ownText());
    }

    /*
     * Returns the number of people that have completed the media.
     * Pulls from 'Completed' row of the 'Summary Stats' section
     */
    public int parseCompleted() {
        Element completed = parent(completedSelector);
        return completed == null ? 0 : parseNumber(completed.ownText());
    }

    /*
     * Returns the number of people that have put the media on hold.
     * Pulls from 'On-Hold' row of the 'Summary Stats' section
     */
    public int parseOnHold() {
        Element onHold = parent(onHoldSelector);
        return onHold == null ? 0 : parseNumber(onHold.ownText());
    }

    /*
     * Returns the number of people that have dropped the media.
     * Pulls from 'Dropped' row of the 'Summary Stats' section
     */
    public int parseDropped() {
        Element dropped = parent(droppedSelector);
        return dropped == null ? 0 : parseNumber(dropped.ownText());
    }

    /*
     * Returns the number of people that have registered
     * the media on their list. Pulls from 'Total' row of
     * the 'Summary Stats' section
     */
    public int parseTotal() {
        Element total = parent(totalSelector);
        return total == null ? 0 : parseNumber(total.ownText());
    }

    // TODO: Update parseRatings() to use a custom data structure
    // for returning the information. The current return type
    // is confusing to understand.

    /*
     * Returns a 2D array of ratings, where
     * - row[i][0] - row[i][0] members have given the media a rating level
     *               of 'i', where 'i' is between 1-10
     * - row[i][1] - row[i][1 percentage of members have given the media a
     *               rating level of 'i'
     * Numbers are pulled from the 'Score Stats' section
     */
    public float[][] parseRatings() {
        Element ratingsTable = nextElementSibling(scoreRatingSelector);
        float[][] ratings = new float[11][2];

        if (ratingsTable == null || !ratingsTable.tag().toString().equals("table")) {
            return ratings;
        }

        Element ratingsTableBody = ratingsTable.child(0);

        for (Element ratingLevel: ratingsTableBody.children()) {
            Element levelElem = ratingLevel.selectFirst("td[width='20']");
            int level = Integer.parseInt(levelElem.ownText());

            Element ratingElem = ratingLevel.selectFirst("span");
            String ratingStr = ratingElem.html();

            Matcher ratingMatcher = ratingPattern.matcher(ratingStr);
            ratingMatcher.find();

            float percent = Float.parseFloat(ratingMatcher.group(1));
            float numMembers = Float.parseFloat(ratingMatcher.group(3));
            ratings[level][0] = numMembers;
            ratings[level][1] = percent;
        }

        return ratings;
    }
}
