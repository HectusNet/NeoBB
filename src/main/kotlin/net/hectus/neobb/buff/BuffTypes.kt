package net.hectus.neobb.buff

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.util.*
import net.kyori.adventure.text.format.TextColor
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class ExtraTurn(data: Int = 1, target: BuffTarget = BuffTarget.YOU): Buff<Int>(data, target) {
    override fun apply(source: NeoPlayer) {
        if (source.game.history.size >= Constants.MAX_EXTRA_TURNS && source.game.history.takeLast(Constants.MAX_EXTRA_TURNS).all { turn -> turn is BuffFunction && turn.buffs().any { it is ExtraTurn } }) {
            source.sendMessage("gameplay.info.extra-turn.enough", color = Colors.NEUTRAL)
            return
        }

        val target = this.target.get(source)
        target.addModifier(Modifiers.Player.EXTRA_TURN)

        if (data > 1) { // Not nice, but works. Too lazy to create a proper way.
            for (i in 1..<data) source.game.turnScheduler.runTaskLater(ScheduleID.EXTRA_TURN, i) {
                target.addModifier(Modifiers.Player.EXTRA_TURN)
            }
        }

        source.game.target(false).sendMessage("gameplay.info.extra-turn", target.name(), color = Colors.NEUTRAL)
    }

    override fun text(locale: Locale): String = locale.string("item-lore.buff.extra-turn" + (if (data > 1) "s" else ""), data.toString())

    override fun color(): TextColor {
        return when (target) {
            BuffTarget.YOU -> Colors.POSITIVE
            BuffTarget.ALL -> Colors.NEUTRAL
            BuffTarget.NEXT, BuffTarget.OPPONENTS -> Colors.NEGATIVE
        }
    }
}

class Luck(data: Int, target: BuffTarget = BuffTarget.YOU): Buff<Int>(data, target) {
    override fun apply(source: NeoPlayer) {
        source.addLuck(data)

        source.game.target(false).sendMessage("gameplay.info.luck", target.name, data.toString(), color = Colors.NEUTRAL)
    }

    override fun text(locale: Locale): String = locale.string("item-lore.buff.luck", data.toString())

    override fun color(): TextColor {
        return when (target) {
            BuffTarget.YOU -> if (data > 0) Colors.POSITIVE else Colors.NEGATIVE
            BuffTarget.ALL -> Colors.NEUTRAL
            BuffTarget.NEXT, BuffTarget.OPPONENTS -> if (data > 0) Colors.NEGATIVE else Colors.POSITIVE
        }
    }
}

class Effect(data: PotionEffect, target: BuffTarget = BuffTarget.YOU): Buff<PotionEffect>(data, target) {
    constructor(type: PotionEffectType, amplifier: Int = 0, target: BuffTarget = BuffTarget.YOU): this(PotionEffect(type, -1, amplifier, true, true), target)

    override fun apply(source: NeoPlayer) {
        source.eachBukkitPlayer { it.addPotionEffect(data) }
    }

    override fun text(locale: Locale): String =
        locale.string("item-lore.buff.effect." + data.type.key.key) + (if (data.amplifier == 0) "" else " " + data.amplifier)

    override fun color(): TextColor {
        return when (target) {
            BuffTarget.YOU -> when (data.type.effectCategory) {
                PotionEffectType.Category.BENEFICIAL -> Colors.POSITIVE
                PotionEffectType.Category.NEUTRAL -> Colors.NEUTRAL
                PotionEffectType.Category.HARMFUL -> Colors.NEGATIVE
            }
            BuffTarget.ALL -> Colors.NEUTRAL
            BuffTarget.NEXT, BuffTarget.OPPONENTS -> when (data.type.effectCategory) {
                PotionEffectType.Category.BENEFICIAL -> Colors.NEGATIVE
                PotionEffectType.Category.NEUTRAL -> Colors.NEUTRAL
                PotionEffectType.Category.HARMFUL -> Colors.POSITIVE
            }
        }
    }
}

class Teleport(data: Cord, target: BuffTarget = BuffTarget.YOU): Buff<Cord>(data, target) {
    override fun apply(source: NeoPlayer) {
        this.target.get(source).teleport(data)
    }

    override fun text(locale: Locale): String = locale.string("item-lore.buff.teleportation")
    override fun color(): TextColor = Colors.GREEN
}

class Give(data: Turn<*>, target: BuffTarget = BuffTarget.YOU): Buff<Turn<*>>(data, target) {
    override fun apply(source: NeoPlayer) {
        source.eachNeoPlayer { it.inventory.add(data) }

        source.game.initialPlayers.forEach { it.sendMessage("gameplay.info.give", target.name, data.name(it.locale()), color = Colors.NEUTRAL) }
    }

    override fun text(locale: Locale): String = locale.string("item-lore.buff.give", data.items().stream().mapToInt { it.amount }.sum().toString(), data.items().first().displayName().asString())

    override fun color(): TextColor {
        return when (target) {
            BuffTarget.YOU -> Colors.POSITIVE
            BuffTarget.ALL -> Colors.NEUTRAL
            BuffTarget.NEXT, BuffTarget.OPPONENTS -> Colors.NEGATIVE
        }
    }
}
