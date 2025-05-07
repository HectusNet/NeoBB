package net.hectus.neobb.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.util.Colors
import net.kyori.adventure.text.Component
import org.bukkit.entity.Bee
import org.bukkit.potion.PotionEffectType

class TBee(data: Bee?, cord: Cord?, player: NeoPlayer?) : MobTurn<Bee>(data, cord, player), BuffFunction, NatureClazz {
    override val cost: Int = 3

    override fun apply() {
        // TODO: Remove all flower effects.
        player!!.sendMessage(Component.text("This feature is not yet implemented.", Colors.NEGATIVE))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.OPPONENTS))
    }
}
