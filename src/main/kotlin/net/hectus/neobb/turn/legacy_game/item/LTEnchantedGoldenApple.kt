package net.hectus.neobb.turn.legacy_game.item

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ChancedBuff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.turn.default_game.item.ItemTurn
import net.hectus.neobb.turn.default_game.mob.MobTurn
import org.bukkit.inventory.ItemStack

class LTEnchantedGoldenApple(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), CounterFunction, BuffFunction, NeutralClazz {
    override val cost: Int = 6

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("mobs") { it is MobTurn<*> })
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(ChancedBuff(50.0, Luck(100)))
    }
}
