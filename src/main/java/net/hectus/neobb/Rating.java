package net.hectus.neobb;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Utilities;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// Glicko-2 rating system -> http://www.glicko.net/glicko/glicko2.pdf
public final class Rating {

    public static void updateRankingsWin(@NotNull NeoPlayer winner, @NotNull NeoPlayer loser) {
        updateRankings(winner, loser, WIN_FACTOR, LOSS_FACTOR);

        winner.addWin();
        loser.addLoss();
    }

    public static void updateRankingsDraw(@NotNull NeoPlayer player1, @NotNull NeoPlayer player2) {
        updateRankings(player1, player2, DRAW_FACTOR, DRAW_FACTOR);

        player1.addDraw();
        player2.addDraw();
    }

    private static void updateRankings(@NotNull NeoPlayer player1, @NotNull NeoPlayer player2, double result1, double result2) {
        Rating rating1 = new Rating(player1.elo(), player1.ratingDeviation(), player1.volatility(), player1.lastMatchTimeMillis());
        Rating rating2 = new Rating(player2.elo(), player2.ratingDeviation(), player2.volatility(), player2.lastMatchTimeMillis());

        rating1.updateRating(rating2, result1);
        rating2.updateRating(rating1, result2);

        apply(player1, rating1);
        apply(player2, rating2);
    }

    private static void apply(NeoPlayer player, Rating rating) {
        player.setElo(rating.getRating());
        player.setRatingDeviation(rating.getRatingDeviation());
        player.setVolatility(rating.getVolatility());
        player.setLastMatchTimeMillis(System.currentTimeMillis());
    }

    public static void sendInfo(Audience receiver, @NotNull UUID player) {
        if (NeoBB.DATABASE.contains(player))
            NeoBB.DATABASE.add(Map.of("uuid", player));

        Map<String, Object> data = NeoBB.DATABASE.getRowMap(player);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getKey().equals("uuid")) continue;
            receiver.sendMessage(Component.text(Utilities.capitalizeFirstLetter(entry.getKey()) + ": ", Colors.BLUE).append(Component.text(entry.getValue().toString())));
        }
    }

    private static final int MILLIS_IN_A_DAY = 86400000;

    private static final int RATING_PERIOD_DAYS = 30;
    private static final int INITIAL_ELO = 1500;
    private static final int MAX_RATING_DEVIATION = 350;
    private static final double SCALE_CONVERSION_MULT = 173.7178;
    private static final double TAU = 0.2;
    private static final double EPSILON = 1e-6;

    private static final double WIN_FACTOR = 1.0;
    private static final double DRAW_FACTOR = 0.5;
    private static final double LOSS_FACTOR = 0.0;

    private double rating;
    private double ratingDeviation;
    private double volatility;

    public Rating(double rating, double ratingDeviation, double volatility, long lastMatchTimeMillis) {
        this.rating = (rating - INITIAL_ELO) / SCALE_CONVERSION_MULT;
        this.ratingDeviation = ratingDeviation / SCALE_CONVERSION_MULT;
        this.volatility = volatility;

        if (lastMatchTimeMillis != -1)
            increaseRatingDeviation((int) ((System.currentTimeMillis() - lastMatchTimeMillis) / MILLIS_IN_A_DAY));
    }

    public double getRating() {
        return rating * SCALE_CONVERSION_MULT + INITIAL_ELO;
    }

    public double getRatingDeviation() {
        return ratingDeviation * SCALE_CONVERSION_MULT;
    }

    public double getVolatility() {
        return volatility;
    }

    public void updateRating(Rating opponent, double score) {
        double gOpponent = opponent.g();
        double E = E(opponent.rating, gOpponent);
        double v = 1 / (gOpponent * gOpponent * E * (1 - E));
        double delta = v * gOpponent * (score - E);
        double newVolatility = Math.exp(A(delta, v) / 2);
        double newRD = ratingDeviation * ratingDeviation + newVolatility * newVolatility;

        ratingDeviation = 1 / Math.sqrt(1 / newRD + 1 / v);
        rating += ratingDeviation * ratingDeviation * gOpponent * (score - E);
        volatility = Math.sqrt(newVolatility);
    }

    private double g() {
        return 1 / Math.sqrt(1 + 3 * ratingDeviation * ratingDeviation / Math.PI / Math.PI);
    }

    private double E(double opponentRating, double gOpponent) {
        return 1 / (1 + Math.exp(-gOpponent * (rating - opponentRating)));
    }

    private double f(double x, double subtraction, double rdSquaredPlusV, double a) {
        double expX = Math.exp(x);
        double leftSide = expX * (subtraction - expX) / 2 / (rdSquaredPlusV + expX) / (rdSquaredPlusV + expX);
        double rightSide = (x - a) / TAU / TAU;
        return leftSide - rightSide;
    }

    // Illinois Algorithm
    private double A(double delta, double v) {
        double a = Math.log(volatility * volatility);
        double A = a;

        double rdSquaredPlusV = ratingDeviation * ratingDeviation + v;
        double subtraction = delta * delta - rdSquaredPlusV;

        double B;
        if (subtraction > 0) {
            B = Math.log(subtraction);
        }
        else {
            int k = 1;
            B = a - k * TAU;
            while (f(B, subtraction, rdSquaredPlusV, a) < 0) {
                k++;
                B = a - k * TAU;
            }
        }

        double fA = f(A, subtraction, rdSquaredPlusV, a);
        double fB = f(B, subtraction, rdSquaredPlusV, a);
        while (Math.abs(B - A) > EPSILON) {
            double C = A + (A - B) * fA / (fB - fA);
            double fC = f(C, subtraction, rdSquaredPlusV, a);

            if (fC * fB <= 0) {
                A = B;
                fA = fB;
            }
            else {
                fA = fA / 2;
            }

            B = C;
            fB = fC;
        }

        return A;
    }

    private void increaseRatingDeviation(int daysInactive) {
        for (int i = 0; i < daysInactive / RATING_PERIOD_DAYS; i++) {
            ratingDeviation = Math.sqrt(ratingDeviation * ratingDeviation + volatility * volatility);
        }
    }
}
