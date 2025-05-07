package net.hectus.neobb.lore

import com.marcpg.libpg.text.Formatter
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.component
import net.hectus.neobb.util.string
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

abstract class ItemLoreBuilder {
    companion object {
        val SEPARATOR = Component.text("                    ", Colors.EXTRA)
    }

    protected var turn: Turn<*>? = null

    abstract fun build(locale: Locale): List<Component>

    fun buildWithTooltips(locale: Locale): List<Component> {
        val lore = build(locale).toMutableList()
        lore.add(SEPARATOR)
        lore.add(keybindPrefix("key.mouse.left").append(locale.component("item-lore.tooltip.buy")))
        lore.add(keybindPrefix("key.mouse.right").append(locale.component("item-lore.tooltip.buy_3")))
        lore.add(keybindPrefix("key.mouse.left").append(Component.text("+ ")).append(keybindPrefix("key.sneak")).append(locale.component("item-lore.tooltip.buy_max")))
        lore.add(Component.space())
        lore.add(keybindPrefix("key.drop").append(locale.component("item-lore.tooltip.sell")))
        return lore
    }

    fun turn(turn: Turn<*>): ItemLoreBuilder {
        this.turn = turn
        return this
    }

    protected fun key(locale: Locale, key: String, icon: String): Component {
        return Component.text("$icon ").append(locale.component(key)).append(Component.text(": "))
            .color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    protected fun longText(l: Locale, type: String): List<Component> {
        val text = l.string("$type." + turn?.namespace())
        return Formatter.lineWrap(text, 50).map { Component.text("   | $it", Colors.BLUE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
    }

    protected fun keybindPrefix(key: String): Component {
        return Component.text("[")
            .append(Component.keybind(key))
            .append(Component.text("] "))
    }
}
