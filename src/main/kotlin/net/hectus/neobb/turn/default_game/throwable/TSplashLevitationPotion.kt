package net.hectus.neobb.turn.default_game.throwable

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.util.Modifiers
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TSplashLevitationPotion(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), BuffFunction, SupernaturalClazz {
    override val cost: Int = 4

    override fun item(): ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { meta: ItemMeta -> (meta as PotionMeta).addCustomEffect(PotionEffect(PotionEffectType.LEVITATION, 200, 0, true), true) }
        .editMeta { meta: ItemMeta -> (meta as PotionMeta).color = Color.WHITE }
        .build()

    override fun apply() {
        if (player!!.player.hasPotionEffect(PotionEffectType.LEVITATION)) player.removeModifier(Modifiers.Player.EXTRA_TURN)

        player.game.turnScheduler.runTaskLater(ScheduleID.LEVITATION, 3) {
            player.opponents(true).forEach { p ->
                if (p.player.hasPotionEffect(PotionEffectType.LEVITATION))
                    player.game.eliminate(p)
            }
        }
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}