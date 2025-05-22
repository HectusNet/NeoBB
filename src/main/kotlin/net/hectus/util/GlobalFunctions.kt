package net.hectus.util

import com.marcpg.libpg.data.time.Time
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.scheduler.BukkitTask

inline fun <reified T : Enum<T>> enumValueNoCase(name: String): T = enumValues<T>().firstOrNull { it.name.equals(name, ignoreCase = true) }
    ?: throw IllegalArgumentException("No enum constant ${T::class.qualifiedName}.$name")

fun bukkitRun(task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTask(NeoBB.Companion.PLUGIN, task)
fun bukkitRunLater(delay: Time, task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTaskLater(NeoBB.Companion.PLUGIN, task, delay.get() * 20)
fun bukkitRunLater(delay: Long, task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTaskLater(NeoBB.Companion.PLUGIN, task, delay)
fun bukkitRunTimer(delay: Long, interval: Long, task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTaskTimer(NeoBB.Companion.PLUGIN, task, delay, interval)
fun cancelEvent(event: Cancellable, eventPlayer: Player, requireStarted: Boolean, additionalPredicate: (NeoPlayer) -> Boolean) {
    playerEventAction(eventPlayer, requireStarted, additionalPredicate) { p ->
        event.isCancelled = true
        p.playSound(Sound.ENTITY_VILLAGER_NO, 0.5f)
    }
}

fun playerEventAction(eventPlayer: Player, requireStarted: Boolean, additionalPredicate: (NeoPlayer) -> Boolean = { true }, action: (NeoPlayer) -> Unit) {
    val player = GameManager.player(eventPlayer)
    if (player != null && (!requireStarted || player.game.started) && additionalPredicate.invoke(player))
        action.invoke(player)
}
