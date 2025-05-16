package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TDragonHead(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, SupernaturalClazz {
    override val cost: Int = 6

    override fun buffs(): List<Buff<*>> {
        return listOf(
            Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS),
            Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.OPPONENTS),
            Luck(-20, Buff.BuffTarget.OPPONENTS)
        )
    }
}
