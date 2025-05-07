package net.hectus.neobb.lore

import net.kyori.adventure.text.Component
import java.util.*

class DummyItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale): List<Component> = emptyList()
}