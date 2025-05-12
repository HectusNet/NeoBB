package net.hectus.neobb.player

import net.hectus.neobb.NeoBB
import net.hectus.neobb.cosmetic.PlaceParticle
import net.hectus.neobb.cosmetic.PlayerAnimation
import net.hectus.neobb.util.enumValueNoCase
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Registry
import org.bukkit.Sound
import java.util.*

class DatabaseInfo(val uuid: UUID) {
    var elo: Double private set
    var ratingDeviation: Double private set
    var volatility: Double private set
    var lastMatchTimeMillis: Long private set
    var wins: Int private set
    var losses: Int private set
    var draws: Int private set
    var turns: Int private set

    var placeParticle: PlaceParticle private set
    var placeSound: Sound private set
    var outline: NamedTextColor private set
    var winAnimation: PlayerAnimation private set
    var deathAnimation: PlayerAnimation private set

    init {
        if (!NeoBB.DATABASE.contains(uuid)) {
            NeoBB.DATABASE.add(mapOf("uuid" to uuid))
            NeoBB.LOG.info("Added player {} to database.", uuid)
        }

        elo = getDouble("elo")
        ratingDeviation = getDouble("rating_deviation")
        volatility = getDouble("volatility")
        lastMatchTimeMillis = getLong("last_match_time")
        wins = getInt("wins")
        losses = getInt("losses")
        draws = getInt("draws")
        turns = getInt("turns")

        placeParticle = enumValueNoCase(getString("cosmetic_particle", PlaceParticle.ENCHANTMENT.name))

        placeSound = Registry.SOUNDS.get(Key.key(getString("cosmetic_place_sound", "block.note_block.bell"))) ?: Sound.BLOCK_NOTE_BLOCK_BELL
        outline = NamedTextColor.namedColor(getInt("cosmetic_outline", NamedTextColor.WHITE.value())) ?: NamedTextColor.WHITE
        winAnimation = enumValueNoCase(getString("cosmetic_win_animation", PlayerAnimation.NONE.name))
        deathAnimation = enumValueNoCase(getString("cosmetic_death_animation", PlayerAnimation.NONE.name))
    }

    private fun getInt(column: String, default: Int = 0): Int {
        return get(column, default) as Int
    }

    private fun getLong(column: String, default: Long = 0L): Long {
        return get(column, default) as Long
    }

    private fun getDouble(column: String, default: Double = 0.0): Double {
        return get(column, default) as Double
    }

    private fun getString(column: String, default: String = ""): String {
        return get(column, default) as String
    }

    private fun get(column: String, default: Any): Any {
        return NeoBB.DATABASE.get(uuid, column, default)
    }

    fun setElo(elo: Double) {
        this.elo = elo
        NeoBB.DATABASE.set(uuid, "elo", elo)
    }

    fun setRatingDeviation(ratingDeviation: Double) {
        this.ratingDeviation = ratingDeviation
        NeoBB.DATABASE.set(uuid, "rating_deviation", ratingDeviation)
    }

    fun setVolatility(volatility: Double) {
        this.volatility = volatility
        NeoBB.DATABASE.set(uuid, "volatility", volatility)
    }

    fun setLastMatchTimeMillis(lastMatchTimeMillis: Long) {
        this.lastMatchTimeMillis = lastMatchTimeMillis
        NeoBB.DATABASE.set(uuid, "last_match_time", lastMatchTimeMillis)
    }

    fun addWin() {
        wins++
        NeoBB.DATABASE.set(uuid, "wins", wins)
    }

    fun addLoss() {
        losses++
        NeoBB.DATABASE.set(uuid, "losses", losses)
    }

    fun addDraw() {
        draws++
        NeoBB.DATABASE.set(uuid, "draws", draws)
    }

    fun addTurn() {
        turns++
        NeoBB.DATABASE.set(uuid, "turns", turns)
    }
}
