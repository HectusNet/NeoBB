package net.hectus.neobb.lore

import com.marcpg.libpg.lang.Translation
import net.hectus.neobb.turn.ComboTurn
import net.hectus.neobb.turn.default_game.attribute.function.*
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import java.util.*

class LegacyItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale): List<Component> {
        val lore = mutableListOf<Component>()

        lore.add(key(locale, "info.type.type", "❖").color(Colors.YELLOW).append(locale.component("info.type.${type()}", color = Colors.RESET)))
        lore.add(key(locale, "info.hotbar.hotbar", "❖").color(Colors.YELLOW).append(locale.component("info.hotbar.both", color = Colors.RESET))) // TODO: Implement dual-hotbar system.

        val rarity = rarity()
        lore.add(key(locale, "item-lore.rarity", "❖").color(Colors.YELLOW).append(Component.text(rarity.second, Colors.RESET).appendSpace()).append(locale.component("info.rarity." + rarity.first, color = Colors.RESET)))

        lore.add(key(locale, "item-lore.description", "❖"))
        lore.addAll(longText(locale, "description"))

        if (turn is CounterFunction) {
            lore.add(key(locale, "item-lore.counters", "⚔"))
            lore.addAll((turn as CounterFunction).counters().map { it.line(locale) })
        }

        if (turn?.requiresUsageGuide == true) {
            lore.add(SEPARATOR)
            lore.add(key(locale, "item-lore.usage", "➽"))
            lore.addAll(longText(locale, "usage"))
        }

        if (turn is BuffFunction) {
            lore.add(SEPARATOR)
            lore.add(key(locale, "item-lore.buffs", "↑"))
            lore.addAll((turn as BuffFunction).buffs().map { it.line(locale) })
        }

        lore.add(SEPARATOR)
        val quality = quality()
        lore.add(key(locale, "item.quality.quality", "✪").color(Colors.YELLOW).append(Component.text(quality.second, Colors.RESET).appendSpace()).append(locale.component("info.quality." + quality().first, color = Colors.RESET)))
        lore.add(key(locale, "item-lore.cost.key", "₪").color(Colors.YELLOW).append(Translation.component(locale, "item-lore.cost.value", turn?.cost ?: 10).color(Colors.RESET)))

        return lore
    }

    private fun type(): String {
        if (turn is WarpFunction) return "warp"
        if (turn is ComboTurn) return "await"
        if (turn is CounterattackFunction) return "counterattack"
        if (turn is AttackFunction) return "attack"
        if (turn is CounterFunction) return "counter"
        if (turn is BuffFunction) return "buff"
        if (turn is DefenseFunction) return "defense"
        return "other"
    }

    private fun rarity(): Pair<String, String> {
        return Pair("common", "✫") // TODO: Use some cool logic to determine the item rarity.
    }

    private fun quality(): Pair<String, Char> {
        if ((turn?.cost ?: 10) >= 7) return Pair("perfect", '➄')
        if ((turn?.cost ?: 10) >= 5) return Pair("good", '➃')
        if ((turn?.cost ?: 10) >= 4) return Pair("decent", '➂')
        if ((turn?.cost ?: 10) >= 3) return Pair("bad", '➁')
        return Pair("poor", '➀')
    }
}
