package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterattackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TBlackWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, BuffFunction, NeutralClazz {
    override val cost: Int = 5

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.ALL))
    }
}
