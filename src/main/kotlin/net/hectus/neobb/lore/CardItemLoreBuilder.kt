package net.hectus.neobb.lore

import net.kyori.adventure.text.Component
import java.util.*

class CardItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale): List<Component> {
        val lore = mutableListOf<Component>()

        lore.add(SEPARATOR)
        lore.add(key(locale, "item-lore.damage", "❖").append(Component.text(turn?.damage ?: 0.0)))
        if (turn?.requiresUsageGuide == true) {
            lore.add(SEPARATOR)
            lore.add(key(locale, "item-lore.usage", "➽"))
            lore.addAll(longText(locale, "usage.card-game"))
        }

        return lore
    }
}