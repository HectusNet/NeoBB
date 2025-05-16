package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TPiston(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction, RedstoneClazz {
    override val cost: Int = 6

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("all") { true })
    }
}
