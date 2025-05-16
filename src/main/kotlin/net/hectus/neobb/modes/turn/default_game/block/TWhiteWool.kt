package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterbuffFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TWhiteWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction, ColdClazz {
    override val cost: Int = 5

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(RedstoneClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.SLOWNESS, 9, Buff.BuffTarget.ALL))
    }
}
