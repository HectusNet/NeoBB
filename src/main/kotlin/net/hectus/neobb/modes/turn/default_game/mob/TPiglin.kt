package net.hectus.neobb.modes.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.EventFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.entity.Piglin
import org.bukkit.potion.PotionEffectType

class TPiglin(data: Piglin?, cord: Cord?, player: NeoPlayer?) : MobTurn<Piglin>(data, cord, player), EventFunction, HotClazz {
    override val cost: Int = 3

    override fun triggerEvent() {
        data!!.addScoreboardTag(player!!.game.id)
    }

    fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.BLINDNESS), Effect(PotionEffectType.SLOWNESS), Luck(30))
    }
}
