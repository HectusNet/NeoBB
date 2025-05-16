package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TBlueIce(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction, ColdClazz {
    override val cost: Int = 4

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
}
