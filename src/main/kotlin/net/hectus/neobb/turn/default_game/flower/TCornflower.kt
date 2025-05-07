package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TCornflower(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 5

    override fun apply() {
        player!!.game.allowed.add(WaterClazz::class)
        player.game.world.setStorm(true)
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.DARKNESS, target = Buff.BuffTarget.ALL))
    }
}
