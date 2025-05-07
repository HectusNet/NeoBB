package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

class TSponge(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction, WaterClazz {
    override val cost: Int = 5
    override val requiresUsageGuide: Boolean = true

    override fun apply() {
        if (player!!.game.history.isEmpty()) return

        if (player.game.history.last() is TWater) {
            player.game.world.setStorm(true)
            Luck(10).apply(player)
            Effect(PotionEffectType.JUMP_BOOST).apply(player)
        }
    }

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn(), Luck(5))
    }
}