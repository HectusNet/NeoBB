package net.hectus.neobb.modes.lore

import com.marcpg.libpg.lang.Translation
import net.hectus.neobb.modes.turn.ComboTurn
import net.hectus.neobb.modes.turn.default_game.attribute.function.*
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import java.util.*

class LegacyItemLoreBuilder: ItemLoreBuilder() {
    override fun build(locale: Locale): List<Component> {
        val lore = mutableListOf<Component>()

        lore += key(locale, "info.type.type", "❖").color(Colors.YELLOW).append(locale.component("info.type.${type()}", color = Colors.RESET))
        lore += key(locale, "info.hotbar.hotbar", "❖").color(Colors.YELLOW).append(locale.component("info.hotbar.both", color = Colors.RESET)) // TODO: Implement dual-hotbar system.

        val rarity = rarity()
        lore += key(locale, "item-lore.rarity", "❖").color(Colors.YELLOW).append(Component.text(rarity.second, Colors.RESET).appendSpace()).append(locale.component("info.rarity." + rarity.first, color = Colors.RESET))

        lore += key(locale, "item-lore.description", "❖")
        lore += longText(locale, "description")

        if (turn is CounterFunction) {
            lore += key(locale, "item-lore.counters", "⚔")
            lore += (turn as CounterFunction).counters().map { it.line(locale) }
        }

        if (locale.translationExists("usage")) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.usage", "➽")
            lore += longText(locale, "usage")
        }

        if (turn is BuffFunction) {
            lore += SEPARATOR
            lore += key(locale, "item-lore.buffs", "↑")
            lore += (turn as BuffFunction).buffs().map { it.line(locale) }
        }

        lore += SEPARATOR
        val quality = quality()
        lore += key(locale, "item.quality.quality", "✪").color(Colors.YELLOW).append(Component.text(quality.second, Colors.RESET).appendSpace()).append(locale.component("info.quality." + quality.first, color = Colors.RESET))
        lore += key(locale, "item-lore.cost.key", "₪").color(Colors.YELLOW).append(Translation.component(locale, "item-lore.cost.value", turn?.cost ?: 10).color(Colors.RESET))

        return lore
    }

    private fun type(): String {
        return when (turn) {
            is WarpFunction -> "warp"
            is ComboTurn -> "await"
            is CounterattackFunction -> "counterattack"
            is AttackFunction -> "attack"
            is CounterFunction -> "counter"
            is BuffFunction -> "buff"
            is DefenseFunction -> "defense"
            else -> "other"
        }
    }

    private fun rarity(): Pair<String, String> = Pair("common", "✫") // TODO: Use some cool logic to determine the item rarity.

    private fun quality(): Pair<String, Char> {
        return when {
            (turn?.cost ?: 10) >= 7 -> Pair("perfect", '➄')
            (turn?.cost ?: 10) >= 5 -> Pair("good", '➃')
            (turn?.cost ?: 10) >= 4 -> Pair("decent", '➂')
            (turn?.cost ?: 10) >= 3 -> Pair("bad", '➁')
            else -> Pair("poor", '➀')
        }
    }
}
