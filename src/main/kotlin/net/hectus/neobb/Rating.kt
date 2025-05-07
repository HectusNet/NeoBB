package net.hectus.neobb

import net.hectus.neobb.player.DatabaseInfo
import kotlin.math.*

class Rating(originalRating: Double, originalRatingDeviation: Double, private var volatility: Double, lastMatchTimeMillis: Long) {
    companion object {
        private const val MILLIS_IN_A_DAY = 86400000
        private const val RATING_PERIOD_DAYS = 30
        private const val INITIAL_ELO = 1500
        private const val SCALE_CONVERSION_MULTIPLIER = 173.7178
        private const val TAU = 0.2
        private const val EPSILON = 1e-6

        private const val WIN_FACTOR = 1.0
        private const val DRAW_FACTOR = 0.5
        private const val LOSS_FACTOR = 0.0

        fun updateRankingsWin(winner: DatabaseInfo, loser: DatabaseInfo, overallFactor: Double) {
            updateRankings(winner, loser, WIN_FACTOR, LOSS_FACTOR, overallFactor)
            winner.addWin()
            loser.addLoss()
        }

        fun updateRankingsDraw(player1: DatabaseInfo, player2: DatabaseInfo, overallFactor: Double) {
            updateRankings(player1, player2, DRAW_FACTOR, DRAW_FACTOR, overallFactor)
            player1.addDraw()
            player2.addDraw()
        }

        private fun updateRankings(player1: DatabaseInfo, player2: DatabaseInfo, result1: Double, result2: Double, overallFactor: Double) {
            val rating1 = Rating(player1.elo, player1.ratingDeviation, player1.volatility, player1.lastMatchTimeMillis)
            val rating2 = Rating(player2.elo, player2.ratingDeviation, player2.volatility, player2.lastMatchTimeMillis)

            rating1.updateRating(rating2, result1 * overallFactor)
            rating2.updateRating(rating1, result2 * overallFactor)

            apply(player1, rating1)
            apply(player2, rating2)
        }

        private fun apply(player: DatabaseInfo, rating: Rating) {
            player.setElo(rating.getRating())
            player.setRatingDeviation(rating.getRatingDeviation())
            player.setVolatility(rating.getVolatility())
            player.setLastMatchTimeMillis(System.currentTimeMillis())
        }
    }

    private var rating: Double = (originalRating - INITIAL_ELO) / SCALE_CONVERSION_MULTIPLIER
    private var ratingDeviation: Double = originalRatingDeviation / SCALE_CONVERSION_MULTIPLIER

    init {
        if (lastMatchTimeMillis != -1L) {
            val daysInactive = (System.currentTimeMillis() - lastMatchTimeMillis) / MILLIS_IN_A_DAY
            increaseRatingDeviation(daysInactive.toInt())
        }
    }

    fun getRating(): Double {
        return rating * SCALE_CONVERSION_MULTIPLIER + INITIAL_ELO
    }

    fun getRatingDeviation(): Double {
        return ratingDeviation * SCALE_CONVERSION_MULTIPLIER
    }

    fun getVolatility(): Double = volatility

    fun updateRating(opponent: Rating, score: Double) {
        val gOpponent = opponent.g()
        val e = e(opponent.rating, gOpponent)
        val v = 1.0 / (gOpponent * gOpponent * e * (1 - e))
        val delta = v * gOpponent * (score - e)
        val newVolatility = exp(a(delta, v) / 2.0)
        val newRD = ratingDeviation * ratingDeviation + newVolatility * newVolatility

        ratingDeviation = 1.0 / sqrt(1.0 / newRD + 1.0 / v)
        rating += ratingDeviation * ratingDeviation * gOpponent * (score - e)
        volatility = sqrt(newVolatility)
    }

    private fun g(): Double {
        return 1.0 / sqrt(1 + 3 * ratingDeviation * ratingDeviation / PI / PI)
    }

    private fun e(opponentRating: Double, gOpponent: Double): Double {
        return 1.0 / (1 + exp(-gOpponent * (rating - opponentRating)))
    }

    private fun f(x: Double, subtraction: Double, rdSquaredPlusV: Double, a: Double): Double {
        val expX = exp(x)
        val leftSide = expX * (subtraction - expX) / 2.0 / (rdSquaredPlusV + expX) / (rdSquaredPlusV + expX)
        val rightSide = (x - a) / (TAU * TAU)
        return leftSide - rightSide
    }

    private fun a(delta: Double, v: Double): Double {
        val a = ln(volatility * volatility)
        var currentA = a

        val rdSquaredPlusV = ratingDeviation * ratingDeviation + v
        val subtraction = delta * delta - rdSquaredPlusV

        var b = if (subtraction > 0) {
            ln(subtraction)
        } else {
            var k = 1
            var tempB = a - k * TAU
            while (f(tempB, subtraction, rdSquaredPlusV, a) < 0) {
                k++
                tempB = a - k * TAU
            }
            tempB
        }

        var fA = f(currentA, subtraction, rdSquaredPlusV, a)
        var fB = f(b, subtraction, rdSquaredPlusV, a)

        while (abs(b - currentA) > EPSILON) {
            val c = currentA + (currentA - b) * fA / (fB - fA)
            val fC = f(c, subtraction, rdSquaredPlusV, a)

            if (fC * fB <= 0) {
                currentA = b
                fA = fB
            } else {
                fA /= 2.0
            }

            b = c
            fB = fC
        }

        return currentA
    }

    private fun increaseRatingDeviation(daysInactive: Int) {
        val periods = daysInactive / RATING_PERIOD_DAYS
        repeat(periods) {
            ratingDeviation = sqrt(ratingDeviation * ratingDeviation + volatility * volatility)
        }
    }
}
