package hibernateUtils.hibernateConverters.abstractConverters;

import hibernateUtils.hibernateMappings.abstractMappings.AnimeMangaStatistic;
import scrapers.abstractScrapers.AnimeAndMangaStatisticPage;

public abstract class AnimeAndMangaStatisticConverter extends Converter {
    public AnimeAndMangaStatisticConverter() {}

    protected void convertCommonStats(AnimeMangaStatistic stat, AnimeAndMangaStatisticPage page) {
        convertCommonPopularityStats(stat, page);
        convertCommonCompletionStats(stat, page);
        convertCommonRatingStats(stat, page);
    }

    private void convertCommonPopularityStats(AnimeMangaStatistic stat, AnimeAndMangaStatisticPage page) {
        stat.setScore(page.parseScore());
        stat.setRank(page.parseRanked());
        stat.setPopularity(page.parsePopularity());
        stat.setFavorites(page.parseFavorites());
    }

    private void convertCommonCompletionStats(AnimeMangaStatistic stat, AnimeAndMangaStatisticPage page) {
        stat.setCompleted(page.parseCompleted());
        stat.setOnHold(page.parseOnHold());
        stat.setDropped(page.parseDropped());
        stat.setTotal(page.parseTotal());
    }

    private void convertCommonRatingStats(AnimeMangaStatistic stat, AnimeAndMangaStatisticPage page) {
        float[][] ratings = page.parseRatings();

        stat.setRatedOne((int)ratings[1][0]);
        stat.setRatedTwo((int)ratings[2][0]);
        stat.setRatedThree((int)ratings[3][0]);
        stat.setRatedFour((int)ratings[4][0]);
        stat.setRatedFive((int)ratings[5][0]);
        stat.setRatedSix((int)ratings[6][0]);
        stat.setRatedSeven((int)ratings[7][0]);
        stat.setRatedEight((int)ratings[8][0]);
        stat.setRatedNine((int)ratings[9][0]);
        stat.setRatedTen((int)ratings[10][0]);

        stat.setRatedOnePercent(ratings[1][1]);
        stat.setRatedTwoPercent(ratings[2][1]);
        stat.setRatedThreePercent(ratings[3][1]);
        stat.setRatedFourPercent(ratings[4][1]);
        stat.setRatedFivePercent(ratings[5][1]);
        stat.setRatedSixPercent(ratings[6][1]);
        stat.setRatedSevenPercent(ratings[7][1]);
        stat.setRatedEightPercent(ratings[8][1]);
        stat.setRatedNinePercent(ratings[9][1]);
        stat.setRatedTenPercent(ratings[10][1]);
    }
}
