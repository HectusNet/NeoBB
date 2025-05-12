package net.hectus.neobb.game

import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.util.Ticking
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.World
import org.bukkit.entity.Player

abstract class BossBarGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : Game(world, bukkitPlayers, difficulty) {
    open var bossBar: BossBar = BossBar.bossBar(Component.text("Turn Countdown: -"), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.NOTCHED_10)

    override fun extraStart() {
        bossBar.addViewer(target(false))
    }

    override fun extraEnd(force: Boolean) {
        bossBar.removeViewer(target(false))
        bossBar.viewers().forEach { bossBar.removeViewer(it as Audience) }
    }

    override fun extraTick(tick: Ticking.Tick) {
        if (tick.isSecond()) {
            bossBar.name(Component.text("Turn Countdown: ${turnCountdown}s"))
            bossBar.progress((turnCountdown.toFloat() / (info.turnTimer * difficulty.timeFactor).toInt()).coerceIn(0.0f, 1.0f))
        }
    }
}
