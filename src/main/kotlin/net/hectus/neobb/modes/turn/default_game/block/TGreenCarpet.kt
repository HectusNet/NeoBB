package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterattackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TGreenCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, NeutralClazz {
    override val cost: Int = 3

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("wool") { it::class.simpleName!!.endsWith("Wool") })
    }
}
