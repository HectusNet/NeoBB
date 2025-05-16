package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterattackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TSpruceTrapdoor(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, ColdClazz {
    override val cost: Int = 4

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(NatureClazz::class))
    }
}
