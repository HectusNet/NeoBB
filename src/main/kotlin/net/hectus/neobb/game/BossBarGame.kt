package net.hectus.neobb.game

import com.marcpg.libpg.display.SimpleBossBar
import com.marcpg.libpg.util.component
import net.hectus.neobb.game.util.GameDifficulty
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.World
import org.bukkit.entity.Player

abstract class BossBarGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : Game(world, bukkitPlayers, difficulty) {
    open val bossBar: SimpleBossBar = SimpleBossBar(target(false), 20, { component("Turn Countdown: ${turnCountdown}s") }, { (turnCountdown.toFloat() / (info.turnTimer * difficulty.timeFactor).toInt()).coerceIn(0.0f, 1.0f) }, { BossBar.Color.YELLOW }, { BossBar.Overlay.NOTCHED_10 })

    override fun extraStart() = bossBar.start()
    override fun extraEnd(force: Boolean) = bossBar.stop()
}
