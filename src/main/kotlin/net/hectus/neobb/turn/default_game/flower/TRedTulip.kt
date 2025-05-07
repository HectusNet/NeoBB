package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import org.bukkit.block.Block

class TRedTulip(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 4

    override fun apply() {
        player!!.game.allowed.add(RedstoneClazz::class)
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}
