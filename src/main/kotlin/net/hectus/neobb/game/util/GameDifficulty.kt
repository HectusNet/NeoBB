package net.hectus.neobb.game.util

enum class GameDifficulty(val ranked: Boolean, val eloFactor: Double, val completeRules: Boolean, val blockPositionRules: Boolean, val suggestions: Boolean, val timeFactor: Double, val gradientSpeed: Double) {
    EASY(false, 0.0, false, false, true, 2.0, 0.03),
    NORMAL(true, 0.75, true, false, true, 1.0, 0.08),
    HARD(true, 1.0, true, true, false, 0.8, 0.12),
    CHAMP(true, 1.5, true, true, false, 0.4, 0.2),
}
