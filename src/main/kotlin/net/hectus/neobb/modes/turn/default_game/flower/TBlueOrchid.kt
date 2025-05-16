package net.hectus.neobb.modes.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.block.Block

class TBlueOrchid(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 6

    override fun apply() {
        player!!.addModifier(Modifiers.Player.Default.ALWAYS_WARP)
    }
}
