package net.hectus.neobb.modes.lore

import net.kyori.adventure.text.Component
import java.util.*

class DummyItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale, itemClasses: Boolean): List<Component> = emptyList()
}
