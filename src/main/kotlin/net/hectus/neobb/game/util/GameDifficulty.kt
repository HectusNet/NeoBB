package net.hectus.neobb.game.util

enum class GameDifficulty(val ranked: Boolean, val eloFactor: Double, val completeRules: Boolean, val suggestions: Boolean, val timeFactor: Double) {
    EASY(false, 0.0, false, true, 2.0),
    NORMAL(true, 0.75, true, true, 1.0),
    HARD(true, 1.0, true, false, 0.8),
    CHAMP(true, 1.5, true, false, 0.4)
}