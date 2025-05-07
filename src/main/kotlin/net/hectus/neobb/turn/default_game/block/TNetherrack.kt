package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterattackFunction
import org.bukkit.block.Block

class TNetherrack(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, HotClazz {
    override val cost: Int = 4

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(WaterClazz::class))
    }
}