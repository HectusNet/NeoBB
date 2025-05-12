package net.hectus.neobb.shop

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.shop.util.Items
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.person_game.block.*
import net.hectus.neobb.turn.person_game.categorization.*
import net.hectus.neobb.turn.person_game.structure.PTCandleCircle
import net.hectus.neobb.turn.person_game.structure.PTTorchCircle
import net.hectus.neobb.turn.person_game.warp.PTAmethystWarp
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.asString
import net.hectus.neobb.util.bukkitRun
import net.hectus.neobb.util.component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window
import java.util.*
import java.util.stream.Collectors

class PersonShop(player: NeoPlayer) : Shop(player) {
    private val addConsumer: (Map<ItemStack, Turn<*>>, ItemStack) -> Boolean = fun(m, i): Boolean {
        val turn = turn(m, i.type)
        if (player.inventory.allows(turn, i.type)) {
            player.inventory.add(i, turn)
            return true
        }
        player.playSound(Sound.ENTITY_VILLAGER_NO)
        return false
    }

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

            .addIngredient('0', category(locale, PTCandleCircle(null, null, player)) { it is ArmorCategory })
            .addIngredient('1', category(locale, PTBeeNest(null, null, player)) { it is AttackCategory })
            .addIngredient('2', category(locale, PTCake(null, null, player)) { it is BuffCategory })
            .addIngredient('3', category(locale, PTAmethystBlock(null, null, player)) { it is CounterCategory })
            .addIngredient('4', category(locale, PTBlueStainedGlass(null, null, player)) { it is DefensiveCategory })
            .addIngredient('5', category(locale, PTLever(null, null, player)) { it is DefensiveCounterCategory })
            .addIngredient('6', category(locale, PTCandleCircle(null, null, player)) { it is GameEndingCounterCategory })
            .addIngredient('7', category(locale, PTTorchCircle(null, null, player)) { it is SituationalAttackCategory })
            .addIngredient('8', category(locale, PTGlowstone(null, null, player)) { it is UtilityCategory })
            .addIngredient('9', category(locale, PTAmethystWarp(null, Cord(0.0, 0.0, 0.0), player)) { it is WarpCategory })
            .addIngredient('a', category(locale, PTBrainCoral(null, null, player)) { it is WinConCategory })

            .addIngredient('D', Items.ClickItem(ItemBuilder(Material.LIME_DYE)
                .name(locale.component("shop.done.name", color = Colors.ACCENT, decoration = TextDecoration.BOLD))
                .addLore(locale.component("shop.done.lore.1", color = Colors.NEUTRAL))
                .addLore(locale.component("shop.done.lore.2", color = Colors.NEUTRAL))
                .build()
            ) { _, _ -> player.closeInv() })

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

    private fun setContent(filter: (Turn<*>) -> Boolean) {
        gui.setContent(content(dummyTurns.stream()
            .filter(filter)
            .flatMap { t -> t.items().stream().map { i -> Pair(ItemBuilder(i).lore(loreBuilder.turn(t).buildWithTooltips(player.locale())).build(), t) } }
            .sorted(Comparator.comparing { it.first.displayName().asString() })
            .collect(Collectors.toMap({ it.first }, { it.second }, { e, _ -> e }, { linkedMapOf() }))
        ))
    }

    private fun content(items: LinkedHashMap<ItemStack, Turn<*>>): List<Item> = content(items, addConsumer)

    private fun category(locale: Locale, example: Category, filter: (Turn<*>) -> Boolean): Items.ClickItem {
        return Items.ClickItem(ItemBuilder(example.categoryItem)
            .name(locale.component("info.function." + example.categoryName))
            .build()
        ) { _, _ -> setContent(filter) }
    }

    private fun turn(map: Map<ItemStack, Turn<*>>, material: Material): Turn<*> {
        for ((key, value) in map) {
            if (key.type == material) return value
        }
        return Turn.DUMMY
    }
}
