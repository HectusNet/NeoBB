package net.hectus.neobb.modes.lore

import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFunction
import net.hectus.neobb.modes.turn.person_game.Category
import net.hectus.neobb.modes.turn.person_game.PWarpTurn
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

class PersonItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale, itemClasses: Boolean): List<Component> {
        val turn = this.turn
        if (turn !is Category) return emptyList()

        val lore = mutableListOf<Component>()

        if (turn.damage != null && turn.damage!! > 0.0) {
            lore += Component.text("Damage: ", turn.categoryColor, TextDecoration.BOLD)
                .append(component("${turn.damage} hearts."))
        }

        if (turn is CounterFunction) {
            lore += locale.component("item-lore.counters", color = turn.categoryColor, decoration = TextDecoration.BOLD)
                .append(component(turn.counters.joinToString(", ") { it.text(locale) }))
        }
        if (turn is PWarpTurn) {
            lore += Component.text("Probability: ", turn.categoryColor, TextDecoration.BOLD)
                .append(component("${turn.chance}%"))
        }
        if (turn.buffs.isNotEmpty()) {
            lore += turn.buffs.map { b -> Component.text("Probability: ", turn.categoryColor, TextDecoration.BOLD)
                .append(Component.text(b.text(locale) + b.target(locale), b.color())) }.toList()
        }

        return lore
    }
}
