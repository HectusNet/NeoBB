package net.hectus.util.display

import net.hectus.util.bukkitRunTimer
import org.bukkit.entity.Player

abstract class SimpleDisplay(
    val player: Player,
    val updateInterval: Long,
) {
    private var running = false

    fun start() {
        extraStart()
        setup()

        running = true
        bukkitRunTimer(updateInterval, updateInterval) { t ->
            if (running) {
                update()
            } else {
                remove()
                t.cancel()
            }
        }
    }

    protected open fun extraStart() {}

    protected abstract fun setup()
    protected abstract fun update()
    protected abstract fun remove()
}
