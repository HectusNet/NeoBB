package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterattackFunction
import org.bukkit.block.Block

class TBrainCoralBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, WaterClazz {
    override val cost: Int = 4

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(RedstoneClazz::class))
    }
}
