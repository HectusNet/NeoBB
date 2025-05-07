package net.hectus.neobb.turn.card_game

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class CTChest(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 5.0
}