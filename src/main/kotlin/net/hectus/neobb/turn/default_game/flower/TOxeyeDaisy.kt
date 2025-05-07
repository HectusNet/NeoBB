package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ChancedBuff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TOxeyeDaisy(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 3

    override fun buffs(): List<Buff<*>> {
        return listOf(ChancedBuff(25.0, ExtraTurn()))
    }
}
