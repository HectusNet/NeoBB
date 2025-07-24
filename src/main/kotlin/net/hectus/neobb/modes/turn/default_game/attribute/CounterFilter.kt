package net.hectus.neobb.modes.turn.default_game.attribute

import com.marcpg.libpg.lang.string
import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.util.Constants
import net.hectus.neobb.util.noItalic
import net.kyori.adventure.text.Component
import java.util.*

interface CounterFilter {
    companion object {
        fun of(name: String, doCounter: (TurnExec<*>) -> Boolean) = object : CounterFilter {
            override fun doCounter(last: TurnExec<*>): Boolean = doCounter.invoke(last)
            override fun text(locale: Locale): String = locale.string("item-lore.counter.$name")
        }
    }

    fun doCounter(last: TurnExec<*>): Boolean
    fun text(locale: Locale): String

    fun line(locale: Locale): Component = component("${Constants.MINECRAFT_TAB_CHAR}| " + text(locale)).noItalic()
}
