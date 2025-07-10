package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.enumValueNoCase
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.camelToSnake
import net.hectus.neobb.util.counterFilterName
import net.kyori.adventure.text.Component
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KClass

abstract class MobTurn<T : LivingEntity>(data: T?, cord: Cord?, player: NeoPlayer?) : Turn<T>(data, cord, player) {
    override fun item(): ItemStack = ItemStack(enumValueNoCase<Material>((this::class.simpleName ?: "unknown").counterFilterName().camelToSnake() + "_SPAWN_EGG"))
}

class TAxolotl(data: Axolotl?, cord: Cord?, player: NeoPlayer?) : MobTurn<Axolotl>(data, cord, player), BuffFunction, WaterClazz {
    override val cost: Int = 4
    override fun apply() {
        when (data!!.variant) {
            Axolotl.Variant.LUCY -> randomWarp(HotClazz::class)
            Axolotl.Variant.WILD -> randomWarp(NatureClazz::class)
            Axolotl.Variant.GOLD -> randomWarp(SupernaturalClazz::class)
            Axolotl.Variant.CYAN -> randomWarp(WaterClazz::class)
            Axolotl.Variant.BLUE -> player!!.game.win(player)
        }
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(10), Luck(-10, Buff.BuffTarget.OPPONENTS))
    }
    private fun randomWarp(allows: KClass<out Clazz>) {
        player!!.game.warp(player.shop.dummyTurns
            .filter { it is WarpTurn && it.allows.contains(allows) }
            .map { it as WarpTurn }
            .random())
    }
}

class TBee(data: Bee?, cord: Cord?, player: NeoPlayer?) : MobTurn<Bee>(data, cord, player), BuffFunction, NatureClazz {
    override val cost: Int = 3
    override fun apply() {
        // TODO: Remove all flower effects.
        player!!.sendMessage(Component.text("This feature is not yet implemented.", Colors.NEGATIVE))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.OPPONENTS))
    }
}

class TBlaze(data: Blaze?, cord: Cord?, player: NeoPlayer?) : MobTurn<Blaze>(data, cord, player), AttackFunction,
    HotClazz {
    override val cost: Int = 5
    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.NO_WARP.name + "_cold")
        player.game.addModifier(Modifiers.Game.NO_WARP.name + "_water")
    }
}

class TEvoker(data: Evoker?, cord: Cord?, player: NeoPlayer?) : MobTurn<Evoker>(data, cord, player), CounterbuffFunction,
    SupernaturalClazz {
    override val cost: Int = 4
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(20), ExtraTurn())
    }
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("blue-sheep") { turn -> turn is TSheep && turn.data!!.color == DyeColor.BLUE })
    }
}

class TPhantom(data: Phantom?, cord: Cord?, player: NeoPlayer?) : MobTurn<Phantom>(data, cord, player), AttackFunction,
    SupernaturalClazz {
    override val cost: Int = 5
}

class TPiglin(data: Piglin?, cord: Cord?, player: NeoPlayer?) : MobTurn<Piglin>(data, cord, player), EventFunction,
    HotClazz {
    override val cost: Int = 3
    override fun triggerEvent() {
        data!!.addScoreboardTag(player!!.game.id)
    }
    fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.BLINDNESS), Effect(PotionEffectType.SLOWNESS), Luck(30))
    }
}

class TPolarBear(data: PolarBear?, cord: Cord?, player: NeoPlayer?) : MobTurn<PolarBear>(data, cord, player), BuffFunction,
    ColdClazz {
    override val cost: Int = 4
    override fun apply() {
        player!!.addModifier(Modifiers.Player.NO_JUMP)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.SPEED, 3, Buff.BuffTarget.ALL))
    }
}

class TPufferfish(data: PufferFish?, cord: Cord?, player: NeoPlayer?) : MobTurn<PufferFish>(data, cord, player), BuffFunction,
    WaterClazz {
    override val cost: Int = 6
    override fun apply() {
        player!!.game.turnScheduler.runTaskLater(ScheduleID.POISON, 2) {
            if (player.nextPlayer().player.hasPotionEffect(PotionEffectType.POISON))
                player.game.eliminate(player.nextPlayer())
        }
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.POISON, target = Buff.BuffTarget.NEXT))
    }
}

class TSheep(data: Sheep?, cord: Cord?, player: NeoPlayer?) : MobTurn<Sheep>(data, cord, player), BuffFunction,
    SupernaturalClazz {
    override val cost: Int = 4
    override fun apply() {
        when (data!!.color) {
            DyeColor.PINK -> player!!.game.win(player)
            DyeColor.LIGHT_GRAY -> ExtraTurn().invoke(player!!)
            DyeColor.GRAY -> Luck(25).invoke(player!!)
            DyeColor.BLACK -> Luck(-15, Buff.BuffTarget.OPPONENTS).invoke(player!!)
            DyeColor.BROWN -> player!!.inventory.addRandom()
            else -> {
                if (player!!.game.allowed.contains(ColdClazz::class))
                    ExtraTurn().invoke(player)
            }
        }
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(5))
    }
}
