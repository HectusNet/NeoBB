package net.hectus.neobb.turn.default_game.item

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction
import net.hectus.neobb.turn.default_game.block.TCauldron
import net.hectus.neobb.turn.default_game.flower.TDirt
import org.bukkit.inventory.ItemStack

class TIronShovel(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), CounterbuffFunction, NeutralClazz {
    override val cost: Int = 5

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("dirt") { it is TDirt }, CounterFilter.of("cauldron") { it is TCauldron }, CounterFilter.clazz(HotClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}
