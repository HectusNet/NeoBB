package net.hectus.neobb.modes.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.DyeColor
import org.bukkit.entity.Sheep

class TSheep(data: Sheep?, cord: Cord?, player: NeoPlayer?) : MobTurn<Sheep>(data, cord, player), BuffFunction, SupernaturalClazz {
    override val cost: Int = 4

    override fun apply() {
        when (data!!.color) {
            DyeColor.PINK -> player!!.game.win(player)
            DyeColor.LIGHT_GRAY -> ExtraTurn().apply(player!!)
            DyeColor.GRAY -> Luck(25).apply(player!!)
            DyeColor.BLACK -> Luck(-15, Buff.BuffTarget.OPPONENTS).apply(player!!)
            DyeColor.BROWN -> player!!.inventory.addRandom()
            else -> {
                if (player!!.game.allowed.contains(ColdClazz::class))
                    ExtraTurn().apply(player)
            }
        }
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(5))
    }
}
