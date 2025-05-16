package net.hectus.neobb.modes.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TWitherRose(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 5

    override fun apply() {
        player!!.game.allowed.remove(SupernaturalClazz::class)
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(-15, Buff.BuffTarget.NEXT))
    }
}
