package net.hectus.neobb.modes.turn.default_game.throwable

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.entity.Projectile
import org.bukkit.potion.PotionEffectType

class TEnderPearl(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), CounterFunction, NeutralClazz {
    override val cost: Int = 3

    override fun apply() {
        player!!.player.removePotionEffect(PotionEffectType.LEVITATION)
        ExtraTurn().apply(player)
    }

    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.of("levitation") { it is TSplashLevitationPotion },
            CounterFilter.of("walls") { turn -> turn::class.simpleName!!.endsWith("Wall") }
        )
    }
}
