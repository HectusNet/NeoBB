package net.hectus.neobb.modes.shop

import com.marcpg.libpg.display.playSound
import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.lore.ItemLoreBuilder
import net.hectus.neobb.modes.shop.util.Items
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.noItalic
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Sound
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.item.Item

abstract class Shop(val player: NeoPlayer) {
    val loreBuilder: ItemLoreBuilder = player.game.info.loreBuilder.java.getConstructor().newInstance()
    protected var onlyAffordable: Boolean = true

    fun syncedOpen() {
        player.inventory.sync()
        open()
    }

    abstract fun open()

    private fun giveLogic(turn: Turn<*>): Boolean {
        if (turn.items.size > player.inventory.slotsLeft() || !player.inventory.allows(turn)) {
            player.playSound(Sound.ENTITY_VILLAGER_NO)
            return false
        }
        return give(turn)
    }

    open fun give(turn: Turn<*>): Boolean {
        player.inventory.add(turn)
        return true
    }

    fun done() {
        player.inventory.shopDone = true
        if (player.game.allShopDone()) {
            player.game.start()
        } else {
            player.sendMessage(player.locale().component("shop.wait", color = Colors.EXTRA))
        }
    }

    protected fun content(filter: (Turn<*>) -> Boolean): List<Item> = player.game.info.turns
        .filter { it.cost != null && (!onlyAffordable || it.cost!! <= player.inventory.coins) }
        .filter(filter)
        .map { makeShopItem(it) }

    fun makeShopItem(turn: Turn<*>) = Items.ClickItem(makeItem(turn, turn.mainItem, true)) { _, e -> when (e.click) {
        ClickType.LEFT -> giveLogic(turn)
        ClickType.RIGHT -> repeat(3) {
            if (!giveLogic(turn))
                return@ClickItem
        }
        ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> repeat(player.game.info.deckSize) {
            if (!giveLogic(turn))
                return@ClickItem
        }
        else -> e.isCancelled = true
    } }

    fun makeItem(turn: Turn<*>, item: ItemStack, tooltips: Boolean) = item.clone().apply { editMeta {
        it.displayName(component(turn.translation(player.locale())).decorate(TextDecoration.BOLD).noItalic())
        if (tooltips) {
            it.lore(loreBuilder.turn(turn).buildWithTooltips(player.locale(), player.game.difficulty.completeRules))
        } else {
            it.lore(loreBuilder.turn(turn).build(player.locale(), player.game.difficulty.completeRules))
        }
    } }
}
