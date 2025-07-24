package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.item.ItemBuilder
import com.marcpg.libpg.util.bukkitRunLater
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.util.Modifiers
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

abstract class ThrowableTurn(namespace: String) : Turn<Projectile>(namespace) {
    override val event: TurnEvent = TurnEvent.THROW
}

object TEnderPearl : ThrowableTurn("ender_pearl"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 3

    override val counters: List<CounterFilter> = listOf(
        CounterFilter.of("levitation") { it.turn === TSplashLevitationPotion },
        CounterFilter.of("walls") { it.turn.namespace.endsWith("wall") }
    )

    override fun apply(exec: TurnExec<Projectile>) {
        exec.player.player.removePotionEffect(PotionEffectType.LEVITATION)
        ExtraTurn().invoke(exec.player)
    }
}

object TSnowball : ThrowableTurn("snowball"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 6

    override val counters: List<CounterFilter> = listOf(TurnClazz.HOT)

    override val buffs: List<Buff<*>> = listOf(
        ExtraTurn(),
        Luck(15),
        Effect(PotionEffectType.GLOWING, target = Buff.BuffTarget.OPPONENTS)
    )
}

object TSplashJumpBoostPotion : ThrowableTurn("splash_jump_boost_potion"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 3

    override val mainItem: ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { (this as PotionMeta).basePotionType = PotionType.STRONG_LEAPING }
        .build()

    override fun apply(exec: TurnExec<Projectile>) {
        if (exec.player.player.hasPotionEffect(PotionEffectType.JUMP_BOOST))
            exec.player.removeModifier(Modifiers.Player.EXTRA_TURN)
        exec.player.opponents(true).forEach { p ->
            if (p.player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
                p.player.removePotionEffect(PotionEffectType.JUMP_BOOST)
                p.addModifier(Modifiers.Player.NO_JUMP)
                bukkitRunLater(Time(30)) {
                    p.removeModifier(Modifiers.Player.NO_JUMP)
                }
            }
        }
    }
}

object TSplashLevitationPotion : ThrowableTurn("splash_levitation_potion"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 4

    override val mainItem: ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { (this as PotionMeta).addCustomEffect(PotionEffect(PotionEffectType.LEVITATION, 200, 1, true), true) }
        .editMeta { (this as PotionMeta).color = Color.WHITE }
        .build()

    override fun apply(exec: TurnExec<Projectile>) {
        if (exec.player.player.hasPotionEffect(PotionEffectType.LEVITATION))
            exec.player.removeModifier(Modifiers.Player.EXTRA_TURN)
        exec.player.game.turnScheduler.runTaskLater(ScheduleID.LEVITATION, 3) {
            exec.player.opponents(true).forEach { p ->
                if (p.player.hasPotionEffect(PotionEffectType.LEVITATION))
                    p.game.eliminate(p)
            }
        }
    }
}

object TSplashWaterBottle : ThrowableTurn("splash_water_bottle"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 7

    override val mainItem: ItemStack = ItemBuilder(Material.SPLASH_POTION)
        .editMeta { (this as PotionMeta).basePotionType = PotionType.WATER }
        .build()

    override val counters: List<CounterFilter> = listOf(TurnClazz.HOT, TurnClazz.REDSTONE)

    override val buffs: List<Buff<*>> = listOf(
        Luck(20),
        Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.NEXT),
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.NEXT)
    )
}
