package net.hectus.neobb.turn.default_game.throwable

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction
import org.bukkit.entity.Projectile
import org.bukkit.potion.PotionEffectType

class TSnowball(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), CounterbuffFunction, ColdClazz {
    override val cost: Int = 6

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(HotClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn(), Luck(15), Effect(PotionEffectType.GLOWING, target = Buff.BuffTarget.OPPONENTS))
    }
}