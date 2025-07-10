package net.hectus.neobb.modes.lore

import com.marcpg.libpg.lang.string
import com.marcpg.libpg.util.component
import com.marcpg.libpg.util.lineWrap
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Constants
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
        lore += SEPARATOR
        lore += keybindPrefix("key.mouse.left").append(locale.component("item-lore.tooltip.buy"))
        lore += keybindPrefix("key.mouse.right").append(locale.component("item-lore.tooltip.buy_3"))
        lore += keybindPrefix("key.mouse.left").append(Component.text("+ ")).append(keybindPrefix("key.sneak")).append(locale.component("item-lore.tooltip.buy_max"))
        lore += Component.space()
        lore += keybindPrefix("key.drop").append(locale.component("item-lore.tooltip.sell"))
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
        return text.lineWrap(Constants.MAX_LORE_WIDTH).map { Component.text("${Constants.MINECRAFT_TAB_CHAR}| $it", Colors.BLUE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
    }

    protected fun keybindPrefix(key: String): Component = Component.text("[")
        .append(Component.keybind(key))
        .append(Component.text("] "))

    protected fun Locale.translationExists(prefix: String): Boolean {
        val key = prefix + "." + turn?.namespace()
        return string(key) != key
    }
}
