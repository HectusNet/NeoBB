package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterbuffFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TCyanCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction, NeutralClazz {
    override val cost: Int = 4

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(RedstoneClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}
