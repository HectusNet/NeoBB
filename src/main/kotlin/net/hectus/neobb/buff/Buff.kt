package net.hectus.neobb.buff

import com.marcpg.libpg.display.MinecraftReceiver
import com.marcpg.libpg.display.receiver
import com.marcpg.libpg.lang.string
import com.marcpg.libpg.util.component
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Constants
import net.hectus.neobb.util.luckChance
import net.hectus.neobb.util.noItalic
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import java.util.*

abstract class Buff<T>(protected val data: T, protected val target: BuffTarget) {
    enum class BuffTarget(private val getter: (NeoPlayer) -> MinecraftReceiver) {
        YOU({ it }),
        NEXT({ it.targetPlayer() }),
        OPPONENTS({ it.opponents().receiver() }),
        ALL({ it.game.target() });

        operator fun invoke(source: NeoPlayer) = getter(source)
    }

    abstract operator fun invoke(source: NeoPlayer)
    abstract fun text(locale: Locale): String
    abstract fun color(): TextColor

    fun line(locale: Locale): Component = component("${Constants.MINECRAFT_TAB_CHAR}| ${text(locale)}${target(locale)}", color()).noItalic()

    fun target(locale: Locale): String = if (target == BuffTarget.YOU) "" else " " + locale.string("item-lore.buff.${target.name.lowercase()}")
}

class ChancedBuff(data: Double, val buff: Buff<*>, target: BuffTarget = BuffTarget.YOU): Buff<Double>(data, target) {
    override operator fun invoke(source: NeoPlayer) {
        if (data.luckChance(source.luck))
            buff(source)

        source.sendMessage("gameplay.info.chance.fail", buff.text(source.locale()), color = Colors.NEUTRAL)
    }

    override fun text(locale: Locale): String = "$data | ${buff.text(locale)}"
    override fun color(): TextColor = buff.color()
}
