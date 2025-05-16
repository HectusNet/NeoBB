package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterattackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import org.bukkit.block.data.Directional

class TMagentaGlazedTerracotta(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, NeutralClazz {
    override val cost: Int = 4

    override fun unusable(): Boolean {
        if (isDummy()) return true

        return if (player!!.game.history.isNotEmpty() && player.game.history.last() is BlockTurn) {
            data!!.getRelative((data.blockData as Directional).facing) != player.game.history.last().data
        } else true
    }

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(NatureClazz::class))
    }
}
