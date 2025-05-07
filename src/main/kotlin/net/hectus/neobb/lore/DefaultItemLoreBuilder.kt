package net.hectus.neobb.lore

import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*
import kotlin.reflect.full.allSuperclasses

class DefaultItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale): List<Component> {
        val lore = mutableListOf<Component>()

        lore.add(SEPARATOR)
        lore.add(key(locale, "item-lore.cost.key", "₪").append(locale.component("item-lore.cost.value", (turn?.cost ?: 10).toString(), color = NamedTextColor.GOLD)))
        lore.add(SEPARATOR)
        lore.add(key(locale, "info.usage.usage", "❖").append(locale.component("info.usage.${usage()}")))
        lore.add(key(locale, "info.function.function", "❖").append(locale.component("info.function.${function()}")))
        lore.add(key(locale, "info.class.class", "❖").append(locale.component("info.class.${clazz()}")))
        lore.add(SEPARATOR)
        lore.add(key(locale, "item-lore.description", "❖"))
        lore.addAll(longText(locale, "description"))
        if (turn?.requiresUsageGuide == true) {
            lore.add(SEPARATOR)
            lore.add(key(locale, "item-lore.usage", "➽"))
            lore.addAll(longText(locale, "usage"))
        }
        if (turn is CounterFunction) {
            lore.add(SEPARATOR)
            lore.add(key(locale, "item-lore.counters", "🛡"))
            lore.addAll((turn as CounterFunction).counters().map { it.line(locale) })
        }
        if (turn is BuffFunction) {
            lore.add(SEPARATOR)
            lore.add(key(locale, "item-lore.buffs", "↑"))
            lore.addAll((turn as BuffFunction).buffs().map { it.line(locale) })
        }

        return lore
    }

    private fun clazz(): String {
        return turn!!::class.allSuperclasses.firstOrNull { it.simpleName!!.endsWith("Clazz") && it.simpleName!!.length > 5 }?.simpleName?.removeSuffix("Clazz")?.lowercase() ?: "other"
    }

    private fun function(): String {
        return turn!!::class.allSuperclasses.firstOrNull { it.simpleName!!.endsWith("Function") && it.simpleName!!.length > 8 }?.simpleName?.removeSuffix("Function")?.lowercase() ?: "other"
    }

    private fun usage(): String {
        return turn!!::class.simpleName?.removeSuffix("Turn")?.lowercase() ?: "other"
    }
}
