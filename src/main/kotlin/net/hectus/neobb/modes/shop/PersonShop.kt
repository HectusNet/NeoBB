package net.hectus.neobb.modes.shop

import com.marcpg.libpg.display.closeInventory
import com.marcpg.libpg.item.ItemBuilder
import com.marcpg.libpg.util.bukkitRun
import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.shop.util.Items
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.person_game.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window
import java.util.*

class PersonShop(player: NeoPlayer) : Shop(player) {
    private var gui: PagedGui<Item>

    init {
        val locale = player.locale()

        gui = PagedGui.items().setStructure(
            "# 0 1 2 3 4 5 6 #", // # = Border
            "# 7 8 9 a . . . #", // 0-a = Categories
            "# # # # # # # # #", // * = Items
            "* * * * * * * * *", // D = Done Button
            "* * * * * * * * *", // < = Last Page
            "< # # # D # # # >") // > = Next Page
            .setBackground(Items.GRAY_BACKGROUND)
            .addIngredient('#', Items.WHITE_BACKGROUND)

            .addIngredient('0', category(locale, PTCandleCircle) { it is ArmorCategory })
            .addIngredient('1', category(locale, PTBeeNest) { it is AttackCategory })
            .addIngredient('2', category(locale, PTCake) { it is BuffCategory })
            .addIngredient('3', category(locale, PTAmethystBlock) { it is CounterCategory })
            .addIngredient('4', category(locale, PTBlueStainedGlass) { it is DefensiveCategory })
            .addIngredient('5', category(locale, PTLever) { it is DefensiveCounterCategory })
            .addIngredient('6', category(locale, PTCandleCircle) { it is GameEndingCounterCategory })
            .addIngredient('7', category(locale, PTTorchCircle) { it is SituationalAttackCategory })
            .addIngredient('8', category(locale, PTGlowstone) { it is UtilityCategory })
            .addIngredient('9', category(locale, PTAmethystWarp) { it is WarpCategory })
            .addIngredient('a', category(locale, PTBrainCoral) { it is WinConCategory })

            .addIngredient('D', Items.ClickItem(ItemBuilder(Material.LIME_DYE)
                .name(locale.component("shop.done.name", color = Colors.ACCENT, decoration = TextDecoration.BOLD))
                .addLore(locale.component("shop.done.lore.1", color = Colors.NEUTRAL))
                .addLore(locale.component("shop.done.lore.2", color = Colors.NEUTRAL))
                .build()
            ) { _, _ -> player.closeInventory() })

            .addIngredient('<', Items.PageItem(false))
            .addIngredient('>', Items.PageItem(true))

            .addIngredient('*', Markers.CONTENT_LIST_SLOT_HORIZONTAL)

            .build()
    }

    override fun open() {
        Window.single().setTitle("=== Shop ===").addCloseHandler {
            bukkitRun {
                if (player.player.openInventory.topInventory.type != InventoryType.CHEST) done()
            }
        }.setGui(gui).open(player.player)
    }

    private fun category(locale: Locale, example: Category, filter: (Turn<*>) -> Boolean): Items.ClickItem {
        return Items.ClickItem(ItemBuilder(example.categoryItem)
            .name(locale.component("info.function.${example.categoryName}"))
            .build()
        ) { _, _ -> gui.setContent(content(filter)) }
    }
}
