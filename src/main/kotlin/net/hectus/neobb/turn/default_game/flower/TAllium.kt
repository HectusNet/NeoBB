package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TAllium(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 7

    override fun apply() {
        player!!.game.players.forEach { it.inventory.removeRandom() }
    }
}
