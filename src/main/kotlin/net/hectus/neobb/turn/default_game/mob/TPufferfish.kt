package net.hectus.neobb.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import org.bukkit.entity.PufferFish
import org.bukkit.potion.PotionEffectType

class TPufferfish(data: PufferFish?, cord: Cord?, player: NeoPlayer?) : MobTurn<PufferFish>(data, cord, player), BuffFunction, WaterClazz {
    override val cost: Int = 6

    override fun apply() {
        player!!.game.turnScheduler.runTaskLater(ScheduleID.POISON, 2) {
            if (player.nextPlayer().player.hasPotionEffect(PotionEffectType.POISON))
                player.game.eliminate(player.nextPlayer())
        }
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.POISON, target = Buff.BuffTarget.NEXT))
    }
}
