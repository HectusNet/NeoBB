package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class TFire(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction, HotClazz {
    override val cost: Int = 4

    override fun item(): ItemStack = ItemStack(Material.FLINT_AND_STEEL)

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(RedstoneClazz::class), CounterFilter.clazz(NatureClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.ALL), Effect(PotionEffectType.GLOWING, target = Buff.BuffTarget.ALL))
    }
}