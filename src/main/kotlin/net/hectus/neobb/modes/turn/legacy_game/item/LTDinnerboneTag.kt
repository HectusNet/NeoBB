package net.hectus.neobb.modes.turn.legacy_game.item

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.modes.turn.default_game.item.ItemTurn
import net.hectus.neobb.modes.turn.default_game.mob.MobTurn
import net.hectus.neobb.player.NeoPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class LTDinnerboneTag(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), CounterFunction, NeutralClazz {
    override val cost: Int = 2

    override fun item(): ItemStack = ItemBuilder(Material.NAME_TAG)
        .name(Component.text("Dinnerbone"))
        .build()

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("mobs") { it is MobTurn<*> })
    }
}
