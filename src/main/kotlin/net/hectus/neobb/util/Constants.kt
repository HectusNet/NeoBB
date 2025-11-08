package net.hectus.neobb.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import net.hectus.neobb.game.util.GameDifficulty

object Constants {
    const val INITIAL_PLAYER_RATING = 1500.0
    const val MIN_PLAYER_RATING = 250.0
    const val MAX_PLAYER_RD = 350.0

    const val MINECRAFT_TAB_CHAR = "   "
    const val MAX_LORE_WIDTH = 48

    const val GAME_ID_LENGTH = 16
    const val GAME_ID_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789"
    const val GAME_LOG_PREFIX = "G-"
    const val GAME_CLEANUP_DELAY = 5L

    val DEFAULT_DIFFICULTY = GameDifficulty.NORMAL
    const val MAX_EXTRA_TURNS = 3

    const val HIGHLIGHT_SCALE = 0.98f
    const val SPAWN_POINT_RADIUS = 3.0

    const val CHECK_WARP_CLASSES = true

    const val TARGET_MAX_RANGE = 6.0
    const val TARGET_TOLERANCE = 1.2

    const val BASE_LUCK = 20
    const val ATTACK_SUCCESS_CHANCE = 0.8

    val COUNTER_LUCK_LOSS = 3..8
    val COUNTERATTACK_LUCK_LOSS = 5..12

    val JSON: Json = @OptIn(ExperimentalSerializationApi::class) (Json {
        prettyPrint = true
        useAlternativeNames = false
        prettyPrintIndent = "  "
        namingStrategy = JsonNamingStrategy.KebabCase
        decodeEnumsCaseInsensitive = true
        allowComments = true
    })
}
