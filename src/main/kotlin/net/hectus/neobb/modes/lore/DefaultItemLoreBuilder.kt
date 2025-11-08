package net.hectus.neobb.modes.lore

import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.turn.default_game.WarpTurn
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFunction
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Utilities
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*
import kotlin.reflect.full.allSuperclasses

class DefaultItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale, itemClasses: Boolean): List<Component> {
        val lore = mutableListOf<Component>()

        lore += SEPARATOR
        lore += key(locale, "item-lore.cost.key", "₪").append(locale.component("item-lore.cost.value", (turn!!.cost ?: 10).toString(), color = NamedTextColor.GOLD))
        lore += SEPARATOR
        lore += key(locale, "info.usage.usage", "❖").append(locale.component("info.usage.${usage()}"))
        lore += key(locale, "info.function.function", "❖").append(locale.component("info.function.${function()}"))
        if (turn!!.clazz != null)
            lore += key(locale, "info.class.class", "❖").append(component(turn!!.clazz!!.text(locale)))

        if (locale.translationExists("description")) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.description", "❖")
            lore += longText(locale, "description")
        }
        if (locale.translationExists("usage")) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.usage", "➽")
            lore += longText(locale, "usage")
        }

        if (turn is CounterFunction) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.counters", "🛡")
            lore += turn!!.counters.map { it.line(locale) }
        }
        if (turn!!.buffs.isNotEmpty()) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.buffs", "↑")
            lore += turn!!.buffs.map { it.line(locale) }
        }

        if (turn is WarpTurn) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.warp.temperature", "❄").append(locale.component("info.temperature.${(turn as WarpTurn).temperature.name.lowercase()}", color = (turn as WarpTurn).temperature.color))
            lore += key(locale, "item-lore.warp.chance", "%").append(component("${(turn as WarpTurn).chance}%", color = Utilities.colorTransition(Colors.RED, Colors.GREEN, (turn as WarpTurn).chance / 100.0)))

            if (itemClasses) {
                lore += SEPARATOR
                lore += key(locale, "item-lore.warp.allows", "✔")
                lore += (turn as WarpTurn).allows.map { it.line(locale) }
            }
        }

        return lore
    }

    private fun function(): String = turn!!::class.allSuperclasses.firstOrNull { it.simpleName!!.endsWith("Function") && it.simpleName!!.length > 8 }?.simpleName?.removeSuffix("Function")?.lowercase() ?: "other"
    private fun usage(): String = turn!!::class.allSuperclasses.firstOrNull { it.simpleName!!.endsWith("Turn") && it.isAbstract }?.simpleName?.removeSuffix("Turn")?.lowercase() ?: "other"
}
