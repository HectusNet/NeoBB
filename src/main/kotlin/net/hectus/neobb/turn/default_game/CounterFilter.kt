package net.hectus.neobb.turn.default_game

import com.marcpg.libpg.lang.Translation
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.util.counterFilterName
import net.kyori.adventure.text.Component
import java.util.*
import kotlin.reflect.KClass

interface CounterFilter {
    companion object {
        fun of(name: String, doCounter: (Turn<*>) -> Boolean): CounterFilter {
            return object : CounterFilter {
                override fun doCounter(last: Turn<*>): Boolean {
                    return doCounter.invoke(last)
                }

                override fun text(locale: Locale): String {
                    return Translation.string(locale, "item-lore.counter.$name")
                }
            }
        }

        fun clazz(turn: KClass<out Clazz>): CounterFilter {
            return object : CounterFilter {
                override fun doCounter(last: Turn<*>): Boolean {
                    return turn.isInstance(last)
                }

                override fun text(locale: Locale): String {
                    return Translation.string(locale, "info.class." + (this::class.simpleName ?: "unknown").counterFilterName())
                }
            }
        }
    }

    fun doCounter(last: Turn<*>): Boolean
    fun text(locale: Locale): String

    fun line(locale: Locale): Component {
        return Component.text("   | " + text(locale))
    }
}
