package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterattackFunction
import org.bukkit.block.Block

class TGreenBed(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, SupernaturalClazz {
    override val cost: Int = 2

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(NatureClazz::class))
    }
}
