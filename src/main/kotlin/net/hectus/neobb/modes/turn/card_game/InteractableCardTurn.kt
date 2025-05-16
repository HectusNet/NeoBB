package net.hectus.neobb.modes.turn.card_game

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

abstract class InteractableCardTurn(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    var interactions: Int = 0
        private set
    var lastInteraction: Long = System.currentTimeMillis()
        private set

    open val maxInteractions = 3
    open val interactionIntervalMs = 1000

    @Synchronized
    fun interact() {
        if (interactions <= maxInteractions && System.currentTimeMillis() - lastInteraction <= interactionIntervalMs) {
            lastInteraction = System.currentTimeMillis()
            interactions++
            player?.nextPlayer()?.damage(damage)
        }
    }
}
