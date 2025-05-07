package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ChancedBuff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction
import org.bukkit.block.Block

class TFireCoralFan(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction, WaterClazz {
    override val cost: Int = 5

    override fun buffs(): List<Buff<*>> {
        return listOf(ChancedBuff(80.0, ExtraTurn()))
    }

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(RedstoneClazz::class))
    }
}