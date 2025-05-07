package net.hectus.neobb.game.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.Game
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.time.Clock

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
) : java.io.Serializable

fun Game.saveReplay() {
    val replay = GameReplay(
        gameId = id,
        gameType = info.namespace,
        startingHealth = info.startingHealth,
        difficulty = difficulty.name,
        players = initialPlayers.map { it.name() },
        turns = history.map { turn ->
            TurnData(
                player = turn.player?.name() ?: "System",
                turn = turn.namespace(),
                location = Triple(turn.cord?.x?.toInt() ?: 0, turn.cord?.y?.toInt() ?: 0, turn.cord?.z?.toInt() ?: 0),
                damage = turn.damage,
            )
        },
        duration = info.totalTime.get() - timeLeft.get(),
        winner = players.firstOrNull()?.name()
    )

    val filename = "replay-${Clock.System.now()}-$id.json"
    NeoBB.DATA_DIR.resolve("replays").createDirectories().resolve(filename).writeText(Json.encodeToString(replay))
    info("Saved replay data to replays/$filename")
}
