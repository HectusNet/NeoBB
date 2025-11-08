package net.hectus.neobb.modes.lore

import net.kyori.adventure.text.Component
import java.util.*

class CardItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale, itemClasses: Boolean): List<Component> {
        val lore = mutableListOf<Component>()

        lore += SEPARATOR
        lore += key(locale, "item-lore.damage", "❖").append(Component.text(turn!!.damage ?: 0.0))
        if (locale.translationExists("usage")) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.usage", "➽")
            lore += longText(locale, "usage.card-game")
        }

        return lore
    }
}
