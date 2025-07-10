package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.item.ItemBuilder
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.bukkitRunLater
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.material
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

abstract class ThrowableTurn(data: Projectile?, cord: Cord?, player: NeoPlayer?) : Turn<Projectile>(data, cord, player) {
    override fun item(): ItemStack = ItemStack(this::class.material())
}

class TEnderPearl(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), CounterFunction,
    NeutralClazz {
    override val cost: Int = 3
    override fun apply() {
        player!!.player.removePotionEffect(PotionEffectType.LEVITATION)
        ExtraTurn().invoke(player)
    }
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.of("levitation") { it is TSplashLevitationPotion },
            CounterFilter.of("walls") { turn -> turn::class.simpleName!!.endsWith("Wall") }
        )
    }
}

class TSnowball(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), CounterbuffFunction,
    ColdClazz {
    override val cost: Int = 6
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(HotClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn(), Luck(15), Effect(PotionEffectType.GLOWING, target = Buff.BuffTarget.OPPONENTS))
    }
}

class TSplashJumpBoostPotion(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), BuffFunction,
    SupernaturalClazz {
    override val cost: Int = 3
    override fun item(): ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { (this as PotionMeta).basePotionType = PotionType.STRONG_LEAPING }
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

class TSplashLevitationPotion(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), BuffFunction,
    SupernaturalClazz {
    override val cost: Int = 4
    override fun item(): ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { (this as PotionMeta).addCustomEffect(PotionEffect(PotionEffectType.LEVITATION, 200, 1, true), true) }
        .editMeta { (this as PotionMeta).color = Color.WHITE }
        .build()
    override fun apply() {
        if (player!!.player.hasPotionEffect(PotionEffectType.LEVITATION))
            player.removeModifier(Modifiers.Player.EXTRA_TURN)
        player.game.turnScheduler.runTaskLater(ScheduleID.LEVITATION, 3) {
            player.opponents(true).forEach { p ->
                if (p.player.hasPotionEffect(PotionEffectType.LEVITATION))
                    player.game.eliminate(p)
            }
        }
    }
    override fun buffs(): List<Buff<*>> = emptyList()
}

class TSplashWaterBottle(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), CounterbuffFunction,
    WaterClazz {
    override val cost: Int = 7
    override fun item(): ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { (this as PotionMeta).basePotionType = PotionType.WATER }
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
