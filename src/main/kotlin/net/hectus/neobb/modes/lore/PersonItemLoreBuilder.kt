package net.hectus.neobb.modes.lore

import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.modes.turn.person_game.categorization.Category
import net.hectus.neobb.modes.turn.person_game.warp.PWarpTurn
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

class PersonItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale): List<Component> {
        val turn = this.turn
        if (turn !is Category) return emptyList()

        val lore = mutableListOf<Component>()

        if (turn.damage > 0.0) {
            lore += Component.text("Damage: ", turn.categoryColor, TextDecoration.BOLD)
                .append(Component.text("${turn.damage} hearts."))
        }

        if (turn is CounterFunction) {
            lore += locale.component("item-lore.counters", color = turn.categoryColor, decoration = TextDecoration.BOLD)
                .append(Component.text(turn.counters().joinToString(", ") { it.text(locale) }))
        }
        if (turn is PWarpTurn) {
            lore += Component.text("Probability: ", turn.categoryColor, TextDecoration.BOLD)
                .append(Component.text(turn.chance.toString() + "%"))
        }
        if (turn is BuffFunction) {
            lore += turn.buffs().map { b -> Component.text("Probability: ", turn.categoryColor, TextDecoration.BOLD)
                .append(Component.text(b.text(locale) + b.target(locale), b.color())) }.toList()
        }

        return lore
    }
}
