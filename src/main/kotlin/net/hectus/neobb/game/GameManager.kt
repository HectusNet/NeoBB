package net.hectus.neobb.game

import net.hectus.neobb.player.NeoPlayer
import org.bukkit.entity.Player
import java.util.*

object GameManager {
    val GAMES: MutableMap<String, Game> = mutableMapOf()

    private val playerCacheByUUID: MutableMap<UUID, NeoPlayer> = hashMapOf()

    fun add(game: Game) {
        GAMES[game.id] = game
    }

    fun remove(game: Game) {
        GAMES.remove(game.id)
        game.initialPlayers.forEach { playerCacheByUUID.remove(it.uuid()) }
    }

    operator fun get(id: String): Game? {
        return GAMES[id]
    }

    operator fun get(scoreboardIDs: Collection<String>): Game? {
        for (game in GAMES.values) {
            if (scoreboardIDs.contains(game.id)) return game
        }
        return null
    }

    fun player(player: Player, onlyAlive: Boolean = true): NeoPlayer? {
        val cached = playerCacheByUUID[player.uniqueId]
        if (cached != null && (!onlyAlive || cached.game.players.contains(cached)))
            return cached

        for (game in GAMES.values) {
            val neoPlayer = game.player(player, onlyAlive)
            if (neoPlayer != null) {
                playerCacheByUUID[player.uniqueId] = neoPlayer
                return neoPlayer
            }
        }
        return null
    }
}
