package net.hectus.neobb.modes.turn.legacy_game.item

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.modes.turn.default_game.block.TLightningRod
import net.hectus.neobb.modes.turn.default_game.item.ItemTurn
import net.hectus.neobb.player.NeoPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class LTLightningTrident(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), CounterFunction, BuffFunction, NeutralClazz {
    override val cost: Int = 5

    override fun item(): ItemStack = ItemBuilder(Material.TRIDENT)
        .name(Component.text("Lightning Trident"))
        .build()

    override fun apply() {
        player!!.game.world.strikeLightningEffect(location())
    }

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("lightning_rod") { it is TLightningRod })
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.GLOWING), Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS))
    }
}
