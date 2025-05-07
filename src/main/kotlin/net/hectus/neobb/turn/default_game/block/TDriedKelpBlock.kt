package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TDriedKelpBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, WaterClazz {
    override val cost: Int = 3

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.JUMP_BOOST, 2))
    }
}