package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoInventory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TPoppy(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 6

    override fun apply() {
        val players = player!!.game.players

        val temp: NeoInventory = players.first().inventory
        for (i in 0..<players.size - 1) {
            players[i].inventory = players[i + 1].inventory
        }
        players.last().inventory = temp

        players.forEach { it.inventory.switchOwner(it) }
    }
}
