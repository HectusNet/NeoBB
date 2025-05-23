package net.hectus.neobb.modes.turn

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.modes.turn.person_game.categorization.AttackCategory
import net.hectus.neobb.modes.turn.person_game.categorization.CounterCategory
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.*
import net.hectus.util.asLocation
import net.hectus.util.camelToSnake
import net.hectus.util.camelToTitle
import net.hectus.util.counterFilterName
import net.hectus.util.string
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class Turn<T>(val data: T?, val cord: Cord?, val player: NeoPlayer?) {
    companion object {
        val DUMMY: Turn<Unit> = object : Turn<Unit>(null, null, null) {
            override val cost: Int = 10
            override fun goodChoice(player: NeoPlayer): Boolean = false
        }
    }

    open val maxAmount: Int = 2
    open val damage: Double = 0.0
    open val cost: Int = 0

    open fun unusable(): Boolean = false

    open fun item(): ItemStack = ItemStack.empty()
    open fun items(): List<ItemStack> = listOf(item())

    open fun apply() {}

    open fun goodChoice(player: NeoPlayer): Boolean {
        if (!player.game.started) return false

        if (unusable() || !player.game.allows(this)) return false

        if ((player.hasModifier(Modifiers.Player.Default.ATTACKED) || player.game.turnScheduler.exists(ScheduleID.FREEZE)) && !player.hasModifier(Modifiers.Player.Default.DEFENDED)) {
            if (player.game.history.isEmpty()) return true

            val last = player.game.history.last()

            if (this is CounterFunction) {
                return this.counters().any { it.doCounter(last) }
            } else if (this is CounterCategory) {
                return last is AttackCategory && last.counteredBy().contains(this::class)
            }
        }

        return this !is AttackFunction || !player.nextPlayer().hasModifier(Modifiers.Player.Default.DEFENDED)
    }

    fun location(): Location {
        return cord?.asLocation(player!!.game.world) ?: player!!.location()
    }

    fun isDummy(): Boolean = this == DUMMY || data == null

    fun namespace(): String = this::class.java.simpleName.counterFilterName().camelToSnake()

    open fun name(locale: Locale): String {
        val key = "turns.${namespace()}"
        val translation = locale.string(key)
        return if (translation == key) {
            (this::class.simpleName ?: "unknown").counterFilterName().camelToTitle()
        } else {
            translation
        }
    }
}
