package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TAzureBluet(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 7

    override fun unusable(): Boolean {
        if (isDummy()) return true
        return player!!.game.history.none { it is TAzureBluet }
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(20))
    }
}
