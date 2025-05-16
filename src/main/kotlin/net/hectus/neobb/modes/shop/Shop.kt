package net.hectus.neobb.modes.shop

import net.hectus.neobb.modes.lore.ItemLoreBuilder
import net.hectus.neobb.modes.shop.util.Items
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Configuration
import net.hectus.neobb.util.component
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.item.Item
import kotlin.reflect.KClass

abstract class Shop(val player: NeoPlayer) {
    companion object {
        fun turn(clazz: KClass<out Turn<*>>, player: NeoPlayer): Turn<*> {
            return clazz.constructors.first().call(null, Configuration.SPAWN_CORD, player)
        }
    }

    val loreBuilder: ItemLoreBuilder = player.game.info.loreBuilder.java.getConstructor().newInstance()
    val dummyTurns: List<Turn<*>> = player.game.info.turns.map { turn(it, player) }

    fun syncedOpen() {
        player.inventory.sync()
        open()
    }

    abstract fun open()

    fun done() {
        player.inventory.shopDone = true
        if (player.game.allShopDone()) {
            player.game.start()
        } else {
            player.sendMessage(player.locale().component("shop.wait", color = Colors.EXTRA))
        }
    }

    protected fun content(items: LinkedHashMap<ItemStack, Turn<*>>, consumer: (Map<ItemStack, Turn<*>>, ItemStack) -> Boolean): List<Item> {
        return items.sequencedKeySet().map { i -> Items.ClickItem(i) { _, e -> when (e.click) {
            ClickType.LEFT -> consumer(items, i)
            ClickType.RIGHT -> repeat(3) {
                if (!consumer(items, i))
                    return@ClickItem
            }
            ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> repeat(player.game.info.deckSize) {
                if (!consumer(items, i))
                    return@ClickItem
            }
            else -> e.isCancelled = true
        } } }
    }
}
