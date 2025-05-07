package net.hectus.neobb.turn.default_game.throwable

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction
import org.bukkit.Material
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class TSplashWaterBottle(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), CounterbuffFunction, WaterClazz {
    override val cost: Int = 7

    override fun item(): ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { meta: ItemMeta -> (meta as PotionMeta).basePotionType = PotionType.WATER }
        .build()

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(RedstoneClazz::class))
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(
            Luck(20),
            Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.NEXT),
            Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.NEXT)
        )
    }
}