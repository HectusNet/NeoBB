package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterFunction
import org.bukkit.block.Block

class TVerdantFroglight(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction, NeutralClazz {
    override val cost: Int = 2

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(SupernaturalClazz::class))
    }
}