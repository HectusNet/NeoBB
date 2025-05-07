package net.hectus.neobb.shop.util

import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.NeoBB
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.ItemWrapper
import xyz.xenondevs.invui.item.builder.SkullBuilder
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

    class ScrollItem(private val up: Boolean) : xyz.xenondevs.invui.item.impl.controlitem.ScrollItem(if (up) -1 else 1) {
        override fun getItemProvider(gui: ScrollGui<*>): ItemProvider {
            try {
                return SkullBuilder("MHF_Arrow" + (if (up) "Up" else "Down")).setDisplayName(AdventureComponentWrapper(Component.text("Scroll " + (if (up) "Up" else "Down"))))
            } catch (e: Exception) {
                NeoBB.LOG.warn("Could not create skull for scroll item.", e)
                return ItemProvider.EMPTY
            }
        }
    }

    class PageItem(private val forward: Boolean) : xyz.xenondevs.invui.item.impl.controlitem.PageItem(forward) {
        override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
            try {
                return SkullBuilder("MHF_Arrow" + (if (forward) "Right" else "Left")).setDisplayName(AdventureComponentWrapper(Component.text((if (forward) "Next" else "Last") + " Page")))
            } catch (e: Exception) {
                NeoBB.LOG.warn("Could not create skull for page item.", e)
                return ItemProvider.EMPTY
            }
        }
    }
}