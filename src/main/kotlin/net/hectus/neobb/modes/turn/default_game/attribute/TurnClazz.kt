package net.hectus.neobb.modes.turn.default_game.attribute

import com.marcpg.libpg.lang.string
import net.hectus.neobb.modes.turn.TurnExec
import java.util.*

enum class TurnClazz : CounterFilter {
    COLD,
    HOT,
    NATURE,
    NEUTRAL,
    REDSTONE,
    SUPERNATURAL,
    WATER;

    override fun doCounter(last: TurnExec<*>): Boolean = last.turn.clazz == this

    override fun text(locale: Locale): String = locale.string("info.class." + this.name.lowercase())
}
