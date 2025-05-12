package net.hectus.neobb.shop

import net.hectus.neobb.lore.ItemLoreBuilder
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.component
import kotlin.reflect.KClass

abstract class Shop(val player: NeoPlayer) {
    companion object {
        fun turn(clazz: KClass<out Turn<*>>, player: NeoPlayer): Turn<*> {
            return if (clazz.simpleName?.contains("warp", true) == true) {
                clazz.java.getConstructor(PlacedStructure::class.java, NeoPlayer::class.java).newInstance(null, player)
            } else {
                clazz.constructors.first().call(null, null, player)
            }
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
