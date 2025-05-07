package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TSunflower(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 5

    override fun apply() {
        player!!.game.world.clearWeatherDuration = Int.MAX_VALUE
        player.game.world.setStorm(false)
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(10), Effect(PotionEffectType.SPEED, 1, Buff.BuffTarget.ALL))
    }
}
