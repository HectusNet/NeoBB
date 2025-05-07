package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterattackFunction
import org.bukkit.block.Block

class TGoldBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, NeutralClazz {
    override val cost: Int = 5

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(NatureClazz::class))
    }
}