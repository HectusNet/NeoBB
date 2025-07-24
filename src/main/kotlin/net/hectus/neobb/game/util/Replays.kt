package net.hectus.neobb.game.util

import kotlinx.serialization.Serializable
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.Game
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import net.hectus.neobb.util.Constants
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

@Serializable
private data class GameReplay(
    val gameId: String,
    val gameType: String,
    val startingHealth: Double,
    val difficulty: String,
    val players: List<String>,
    val turns: List<TurnData>,
    val duration: Long,
    val winner: String?,
) : java.io.Serializable

@Serializable
private data class TurnData(
    val player: String,
    val turn: String,
    val location: Triple<Int, Int, Int>?,
    val damage: Double,
    val clazz: TurnClazz?,
    val meta: Map<String, String>,
) : java.io.Serializable

fun Game.saveReplay() {
    val replay = GameReplay(
        gameId = id,
        gameType = info.namespace,
        startingHealth = info.startingHealth,
        difficulty = difficulty.name,
        players = initialPlayers.map { it.name() },
        turns = history.map { exec ->
            TurnData(
                player = exec.player.name(),
                turn = exec.turn.namespace,
                location = Triple(exec.cord?.x?.toInt() ?: 0, exec.cord?.y?.toInt() ?: 0, exec.cord?.z?.toInt() ?: 0),
                damage = exec.turn.damage ?: 0.0,
                clazz = exec.turn.clazz,
                meta = exec.meta.mapValues { it.toString() },
            )
        },
        duration = info.totalTime.get() - timeLeft.get(),
        winner = players.firstOrNull()?.name()
    )

    val filename = "replay-${System.currentTimeMillis()}-$id.json"
    NeoBB.DATA_DIR.resolve("replays").createDirectories().resolve(filename).writeText(Constants.JSON.encodeToString(replay))
    info("Saved replay data to replays/$filename")
}
