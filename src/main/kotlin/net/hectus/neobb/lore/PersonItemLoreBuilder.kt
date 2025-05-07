package net.hectus.neobb.lore

import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.turn.person_game.categorization.Category
import net.hectus.neobb.turn.person_game.warp.PWarpTurn
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

class PersonItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale): List<Component> {
        if (turn !is Category) return emptyList()

        val lore = mutableListOf<Component>()

        if ((turn?.damage ?: 0.0) > 0.0) {
            lore.add(
                Component.text("Damage: ", (turn as Category).categoryColor, TextDecoration.BOLD)
                    .append(Component.text("${turn!!.damage} hearts."))
            )
        }

        if (turn is CounterFunction) {
            lore.add(locale.component("item-lore.counters", color = (turn as Category).categoryColor, decoration = TextDecoration.BOLD)
                .append(Component.text((turn as CounterFunction).counters().joinToString(", ") { it.text(locale) })))
        }
        if (turn is PWarpTurn) {
            lore.add(
                Component.text("Probability: ", (turn as Category).categoryColor, TextDecoration.BOLD)
                    .append(Component.text((turn as PWarpTurn).chance.toString() + "%"))
            )
        }
        if (turn is BuffFunction) {
            lore.addAll((turn as BuffFunction).buffs().map { b -> Component.text("Probability: ", (turn as Category).categoryColor, TextDecoration.BOLD)
                .append(Component.text(b.text(locale) + b.target(locale), b.color())) }.toList())
        }

        return lore
    }
}
