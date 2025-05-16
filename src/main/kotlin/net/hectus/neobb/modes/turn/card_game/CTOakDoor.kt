package net.hectus.neobb.modes.turn.card_game

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class CTOakDoor(data: Block?, cord: Cord?, player: NeoPlayer?) : InteractableCardTurn(data, cord, player) {
    override val damage: Double = 4.0
}
