package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterattackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TLightningRod(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, RedstoneClazz {
    override val cost: Int = 6

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(RedstoneClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
}
