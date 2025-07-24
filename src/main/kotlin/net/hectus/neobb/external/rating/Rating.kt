package net.hectus.neobb.external.rating

import net.hectus.neobb.player.DatabaseInfo
import net.hectus.neobb.util.Constants
import kotlin.math.*

@Suppress("LocalVariableName")
class Rating(private val mu: Double, private val phi: Double, private var sigma: Double) {
    companion object {
        private const val SCALE_MULTIPLIER = 173.7178
        private const val TAU = 0.2
        private const val EPSILON = 1e-6

        private fun scaleDown(rating: Double): Double = (rating - Constants.INITIAL_PLAYER_RATING) / SCALE_MULTIPLIER
        private fun scaleDownRD(rd: Double): Double = rd / SCALE_MULTIPLIER
        private fun scaleUp(mu: Double): Double = mu * SCALE_MULTIPLIER + Constants.INITIAL_PLAYER_RATING
        private fun scaleUpRD(phi: Double): Double = phi * SCALE_MULTIPLIER

        fun fromPlayer(player: DatabaseInfo): Rating {
            return Rating(
                mu = scaleDown(player.elo),
                phi = scaleDownRD(player.ratingDeviation),
                sigma = player.volatility
            )
        }

        fun updateFFARankings(players: List<DatabaseInfo>, winner: DatabaseInfo? = null) {
            require(players.size >= 2) { "Need at least 2 players" }

            if (winner == null) {
                players.forEach { it.addDraw() }
            } else {
                players.forEach { if (it == winner) it.addWin() else it.addLoss() }
            }

            val ratings = players.associateWith { fromPlayer(it) }

            val updated = players.associateWith { player ->
                val selfRating = ratings[player]!!
                val updated = players.filter { it != player }.fold(selfRating) { acc, opponent ->
                    val opponentRating = ratings[opponent]!!
                    val score = when {
                        winner == null -> 0.5  // All-draw scenario
                        player == winner -> 1.0
                        opponent == winner -> 0.0
                        else -> 0.5  // Optional: ties among losers
                    }
                    acc.updatedAgainst(opponentRating, score)
                }
                updated
            }

            for ((player, newRating) in updated) {
                applyToPlayer(player, newRating)
            }
        }

        private fun applyToPlayer(player: DatabaseInfo, rating: Rating) {
            val finalRating = scaleUp(rating.mu).coerceAtLeast(Constants.MIN_PLAYER_RATING)
            val finalRD = scaleUpRD(rating.phi).coerceAtMost(Constants.MAX_PLAYER_RD)

            player.setElo(finalRating)
            player.setRatingDeviation(finalRD)
            player.setVolatility(rating.sigma)
            player.setLastMatchTimeMillis(System.currentTimeMillis())
        }
    }

    fun updatedAgainst(opponent: Rating, score: Double): Rating {
        val g = g(opponent.phi)
        val E = expectedScore(opponent.mu, g)
        val v = 1.0 / (g * g * E * (1.0 - E))
        val delta = v * g * (score - E)

        val newSigma = computeNewSigma(delta, v)
        val phiStar = sqrt(phi * phi + newSigma * newSigma)
        val newPhi = 1.0 / sqrt(1.0 / (phiStar * phiStar) + 1.0 / v)
        val newMu = mu + newPhi * newPhi * g * (score - E)

        return Rating(newMu, newPhi, newSigma)
    }

    private fun g(phi: Double): Double {
        return 1.0 / sqrt(1.0 + 3.0 * phi * phi / PI / PI)
    }

    private fun expectedScore(opponentMu: Double, g: Double): Double {
        return 1.0 / (1.0 + exp(-g * (mu - opponentMu)))
    }

    private fun computeNewSigma(delta: Double, v: Double): Double {
        val a = ln(sigma * sigma)
        var A = a
        var B: Double

        if (delta * delta > phi * phi + v) {
            B = ln(delta * delta - phi * phi - v)
        } else {
            var k = 1
            B = a - k * TAU
            while (f(B, delta, v, a) < 0.0) {
                k++
                B = a - k * TAU
            }
        }

        var fA = f(A, delta, v, a)
        var fB = f(B, delta, v, a)

        while (abs(B - A) > EPSILON) {
            val C = A + (A - B) * fA / (fB - fA)
            val fC = f(C, delta, v, a)

            if (fC * fB < 0) {
                A = B
                fA = fB
            } else {
                fA /= 2.0
            }

            B = C
            fB = fC
        }

        return exp(A / 2.0)
    }

    private fun f(x: Double, delta: Double, v: Double, a: Double): Double {
        val expX = exp(x)
        val num = expX * (delta * delta - phi * phi - v - expX)
        val denominator = 2.0 * (phi * phi + v + expX) * (phi * phi + v + expX)
        return num / denominator - (x - a) / (TAU * TAU)
    }
}
