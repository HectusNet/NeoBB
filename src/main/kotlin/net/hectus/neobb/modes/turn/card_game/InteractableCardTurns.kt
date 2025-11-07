package net.hectus.neobb.modes.turn.card_game

import net.hectus.neobb.player.NeoPlayer

abstract class InteractableCardTurn(namespace: String) : CardTurn(namespace) {
    var interactions: Int = 0
        private set
    var lastInteraction: Long = System.currentTimeMillis()
        private set

    open val maxInteractions = 3
    open val interactionIntervalMs = 1000

    @Synchronized
    fun interact(player: NeoPlayer) {
        if (interactions <= maxInteractions && System.currentTimeMillis() - lastInteraction <= interactionIntervalMs) {
            lastInteraction = System.currentTimeMillis()
            interactions++
            player.targetPlayer().damage(damage!!)
        }
    }
}

object CTOakDoor : InteractableCardTurn("oak_door") {
    override val damage: Double = 4.0
}

object CTOakFenceGate : InteractableCardTurn("oak_fence_gate") {
    override val damage: Double = 4.0
}
