package scrapers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AnimeMangaStatisticPage extends MalPage{
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

    public AnimeMangaStatisticPage(Document doc) {
        super(doc);
    }

    public float parseScore() {
        Element score = doc.selectFirst(scoreSelector);
        return score == null || score.ownText().equals("N/A") ? 0 : Float.parseFloat(score.ownText());
    }

    public int parseRanked() {
        Element ranked = parent(rankedSelector);

        if (ranked == null || ranked.ownText().equals("N/A")) {
            return 0;
        }

        String rankedStr = StringUtils.removeStart(ranked.ownText(), "#");
        return Integer.parseInt(rankedStr);
    }

    public int parsePopularity() {
        Element popularity = parent(popularitySelector);

        if (popularity == null) {
            return 0;
        }

        String popularityStr = StringUtils.removeStart(popularity.ownText(), "#");
        return Integer.parseInt(popularityStr);
    }

    public int parseFavorites() {
        Element favorites = parent(favoritesSelector);
        return favorites == null ? 0 : parseNumber(favorites.ownText());
    }

    public int parseCompleted() {
        Element completed = parent(completedSelector);
        return completed == null ? 0 : parseNumber(completed.ownText());
    }

    public int parseOnHold() {
        Element onHold = parent(onHoldSelector);
        return onHold == null ? 0 : parseNumber(onHold.ownText());
    }

    public int parseDropped() {
        Element dropped = parent(droppedSelector);
        return dropped == null ? 0 : parseNumber(dropped.ownText());
    }

    public int parseTotal() {
        Element total = parent(totalSelector);
        return total == null ? 0 : parseNumber(total.ownText());
    }

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
