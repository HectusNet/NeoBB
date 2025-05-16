package net.hectus.neobb.modes.shop.util

import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.NeoBB
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.ItemWrapper
import xyz.xenondevs.invui.item.impl.AbstractItem

object Items {
    val WHITE_BACKGROUND: ItemStack = ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(Component.empty()).build()
    val GRAY_BACKGROUND: ItemStack = ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).build()
    val BLACK_BACKGROUND: ItemStack = ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).build()

    class ClickItem(private val item: ItemStack, private val clickConsumer: (Player, InventoryClickEvent) -> Unit) : AbstractItem() {
        override fun getItemProvider(): ItemProvider {
            return ItemWrapper(item)
        }

        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            clickConsumer.invoke(player, event)
        }
    }

    class ScrollItem(private val up: Boolean) : xyz.xenondevs.invui.item.impl.controlitem.ScrollItem(hashMapOf(
        ClickType.LEFT to if (up) -1 else 1,
        ClickType.SHIFT_LEFT to if (up) -5 else 5
    )) {
        override fun getItemProvider(gui: ScrollGui<*>): ItemProvider {
            try {
                return ItemWrapper(ItemBuilder(Material.FEATHER)
                    .name(Component.text("Scroll " + (if (up) "Up" else "Down")))
                    .lore(listOf(Component.text("+ [SHIFT] Scroll Down x5", NamedTextColor.GRAY)))
                    .build())
            } catch (e: Exception) {
                NeoBB.LOG.warn("Could not create skull for scroll item.", e)
                return ItemProvider.EMPTY
            }
        }
    }

    class PageItem(private val forward: Boolean) : xyz.xenondevs.invui.item.impl.controlitem.PageItem(forward) {
        override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
            try {
                return ItemWrapper(ItemBuilder(Material.FEATHER)
                    .name(Component.text((if (forward) "Next" else "Last") + " Page"))
                    .build())
            } catch (e: Exception) {
                NeoBB.LOG.warn("Could not create skull for page item.", e)
                return ItemProvider.EMPTY
            }
        }
    }
}
