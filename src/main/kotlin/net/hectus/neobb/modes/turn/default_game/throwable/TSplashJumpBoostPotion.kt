package net.hectus.neobb.modes.turn.default_game.throwable

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import net.hectus.util.bukkitRunLater
import org.bukkit.Material
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class TSplashJumpBoostPotion(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), BuffFunction, SupernaturalClazz {
    override val cost: Int = 3

    override fun item(): ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { meta: ItemMeta -> (meta as PotionMeta).basePotionType = PotionType.STRONG_LEAPING }
        .build()

    override fun apply() {
        if (player!!.player.hasPotionEffect(PotionEffectType.JUMP_BOOST))
            player.removeModifier(Modifiers.Player.EXTRA_TURN)

        player.opponents(true).forEach { p ->
            if (p.player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
                p.player.removePotionEffect(PotionEffectType.JUMP_BOOST)
                p.addModifier(Modifiers.Player.NO_JUMP)
                bukkitRunLater(Time(30)) {
                    p.removeModifier(Modifiers.Player.NO_JUMP)
                }
            }
        }
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}
