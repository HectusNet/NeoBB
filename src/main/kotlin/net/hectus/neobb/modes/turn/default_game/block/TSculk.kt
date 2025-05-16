package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterbuffFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TSculk(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction, NeutralClazz {
    override val cost: Int = 4

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(10, Buff.BuffTarget.OPPONENTS), Effect(PotionEffectType.DARKNESS, target = Buff.BuffTarget.OPPONENTS))
    }
}
