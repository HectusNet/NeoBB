package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.util.component
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

abstract class MobTurn<T : LivingEntity>(namespace: String) : Turn<T>(namespace) {
    override val mainItem: ItemStack = ItemStack.of(enumValueOf<Material>("${namespace.uppercase()}_SPAWN_EGG"))

    override val event: TurnEvent = TurnEvent.CUSTOM
}

object TAxolotl : MobTurn<Axolotl>("axolotl"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.WATER
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(
        Luck(10),
        Luck(-10, Buff.BuffTarget.OPPONENTS)
    )

    override fun apply(exec: TurnExec<Axolotl>) {
        when (exec.data.variant) {
            Axolotl.Variant.LUCY -> randomWarp(exec.player, TurnClazz.HOT)
            Axolotl.Variant.WILD -> randomWarp(exec.player, TurnClazz.NATURE)
            Axolotl.Variant.GOLD -> randomWarp(exec.player, TurnClazz.SUPERNATURAL)
            Axolotl.Variant.CYAN -> randomWarp(exec.player, TurnClazz.WATER)
            Axolotl.Variant.BLUE -> exec.player.game.win(exec.player)
        }
    }
    private fun randomWarp(player: NeoPlayer, allows: TurnClazz) {
        player.game.warp(player, player.game.info.turns.filterIsInstance<WarpTurn>().filter { allows in it.allows }.random())
    }
}

object TBee : MobTurn<Bee>("bee"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.NATURE
    override val cost: Int = 3

    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.OPPONENTS))

    override fun apply(exec: TurnExec<Bee>) {
        // TODO: Remove all flower effects.
        exec.player.sendMessage(component("This feature is not yet implemented.", Colors.NEGATIVE))
    }
}

object TBlaze : MobTurn<Blaze>("blaze"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.HOT
    override val cost: Int = 5

    override fun apply(exec: TurnExec<Blaze>) {
        exec.player.game.addModifier(Modifiers.Game.NO_WARP.name + "_cold")
        exec.player.game.addModifier(Modifiers.Game.NO_WARP.name + "_water")
    }
}

object TEvoker : MobTurn<Evoker>("evoker"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.SUPERNATURAL
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(
        Luck(20),
        ExtraTurn()
    )

    override val counters: List<CounterFilter> = listOf(CounterFilter.of("blue-sheep") { it.turn === TSheep && (it.data as Sheep).color == DyeColor.BLUE })
}

object TPhantom : MobTurn<Phantom>("phantom"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.SUPERNATURAL
    override val cost: Int = 5
}

object TPiglin : MobTurn<Piglin>("piglin"), EventFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.HOT
    override val cost: Int = 3

    override val event: TurnEvent = TurnEvent.CUSTOM

    override val buffs: List<Buff<*>> = listOf(
        Effect(PotionEffectType.BLINDNESS),
        Effect(PotionEffectType.SLOWNESS),
        Luck(30)
    )

    override fun triggerEvent(exec: TurnExec<*>) {
        (exec.data as PolarBear).addScoreboardTag(exec.player.game.id)
    }
}

object TPolarBear : MobTurn<PolarBear>("polar_bear"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.COLD
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.SPEED, 3, Buff.BuffTarget.ALL))

    override fun apply(exec: TurnExec<PolarBear>) {
        exec.player.addModifier(Modifiers.Player.NO_JUMP)
    }
}

object TPufferfish : MobTurn<PufferFish>("pufferfish"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.WATER
    override val cost: Int = 6

    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.POISON, target = Buff.BuffTarget.NEXT))

    override fun apply(exec: TurnExec<PufferFish>) {
        exec.player.game.turnScheduler.runTaskLater(ScheduleID.POISON, 2) {
            if (exec.player.targetPlayer().player.hasPotionEffect(PotionEffectType.POISON))
                exec.player.game.eliminate(exec.player.targetPlayer())
        }
    }
}

object TSheep : MobTurn<Sheep>("sheep"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.SUPERNATURAL
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(Luck(5))

    override fun apply(exec: TurnExec<Sheep>) {
        when (exec.data.color) {
            DyeColor.PINK -> exec.player.game.win(exec.player)
            DyeColor.LIGHT_GRAY -> ExtraTurn().invoke(exec.player)
            DyeColor.GRAY -> Luck(25).invoke(exec.player)
            DyeColor.BLACK -> Luck(-15, Buff.BuffTarget.OPPONENTS).invoke(exec.player)
            DyeColor.BROWN -> exec.player.inventory.addRandom()
            else -> {
                if (TurnClazz.COLD in exec.player.game.allowed)
                    ExtraTurn().invoke(exec.player)
            }
        }
    }
}
