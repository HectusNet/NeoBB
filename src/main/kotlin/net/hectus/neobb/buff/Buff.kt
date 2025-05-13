package net.hectus.neobb.buff

import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.player.ForwardingTarget
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.player.Target
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Constants
import net.hectus.neobb.util.string
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import java.util.*

abstract class Buff<T>(protected val data: T, protected val target: BuffTarget) {
    enum class BuffTarget(private val getter: (NeoPlayer) -> Target) {
        YOU({ it }),
        NEXT({ it.nextPlayer() }),
        OPPONENTS({ ForwardingTarget(it.opponents()) }),
        ALL({ it.game.target() });

        fun get(source: NeoPlayer): Target {
            return getter.invoke(source)
        }
    }

    abstract fun apply(source: NeoPlayer)
    abstract fun text(locale: Locale): String
    abstract fun color(): TextColor

    fun line(locale: Locale): Component {
        return Component.text("${Constants.MINECRAFT_TAB_CHAR}| ${text(locale)}${target(locale)}", color())
    }

    fun target(locale: Locale): String {
        return if (target == BuffTarget.YOU) "" else " " + locale.string("item-lore.buff.${target.name.lowercase()}")
    }
}

class ChancedBuff(data: Double, val buff: Buff<*>, target: BuffTarget = BuffTarget.YOU): Buff<Double>(data, target) {
    override fun apply(source: NeoPlayer) {
        if (Randomizer.boolByChance(data))
            buff.apply(source)

        source.sendMessage("gameplay.info.chance.fail", buff.text(source.locale()), color = Colors.NEUTRAL)
    }

    override fun text(locale: Locale): String = "$data | ${buff.text(locale)}"
    override fun color(): TextColor = buff.color()
}
