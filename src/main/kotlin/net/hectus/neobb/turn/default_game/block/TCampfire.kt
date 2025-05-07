package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterattackFunction
import org.bukkit.block.Block

class TCampfire(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, HotClazz {
    override val cost: Int = 5
    override val requiresUsageGuide: Boolean = true

    override fun apply() {
        if (player!!.game.history.isEmpty()) return
        if (player.game.history.last() is TBeeNest) {
            Luck(10).apply(player)
            ExtraTurn().apply(player)
        }
    }

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class),CounterFilter.clazz(HotClazz::class),CounterFilter.clazz(ColdClazz::class))
    }
}