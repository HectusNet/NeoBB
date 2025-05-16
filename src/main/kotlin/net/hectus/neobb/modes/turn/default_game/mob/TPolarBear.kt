package net.hectus.neobb.modes.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.PolarBear
import org.bukkit.potion.PotionEffectType

class TPolarBear(data: PolarBear?, cord: Cord?, player: NeoPlayer?) : MobTurn<PolarBear>(data, cord, player), BuffFunction, ColdClazz {
    override val cost: Int = 4

    override fun apply() {
        player!!.addModifier(Modifiers.Player.NO_JUMP)
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.SPEED, 3, Buff.BuffTarget.ALL))
    }
}
