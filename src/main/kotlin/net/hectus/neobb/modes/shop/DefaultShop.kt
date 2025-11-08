package net.hectus.neobb.modes.shop

import com.marcpg.libpg.display.closeInventory
import com.marcpg.libpg.display.playSound
import com.marcpg.libpg.item.ItemBuilder
import com.marcpg.libpg.util.bukkitRun
import com.marcpg.libpg.util.component
import net.hectus.neobb.NeoBB
import net.hectus.neobb.modes.shop.util.FilterState
import net.hectus.neobb.modes.shop.util.Items
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.*
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window

class DefaultShop(player: NeoPlayer) : Shop(player) {
    private val filter: MutableMap<String, Pair<FilterState, (Turn<*>) -> Boolean>> = hashMapOf()
    private val gui: ScrollGui<Item>

    init {
        val locale = player.locale()

        gui = ScrollGui.items().setStructure(
            "$ # # # # # # # ^", // # = Border
            "# # * * * * * * #", // F = Filters
            "1 # * * * * * * #", // * = Items
            "2 # * * * * * * #", // D = Done Button
            "3 # * * * * * * #", // ^ = Scroll Up
            "4 # # # D # # # v") // v = Scroll Down
            .setBackground(Items.BLACK_BACKGROUND)
            .addIngredient('#', Items.GRAY_BACKGROUND)

            .addIngredient('1', Items.ClickItem(filter(Material.EGG, "usage")) { _, _ ->
                filterUsageMenu(mapOf(
                    "block" to Pair(Material.GRASS_BLOCK) { it is BlockTurn },
                    "flower" to Pair(Material.POPPY) { it is FlowerTurn },
                    "item" to Pair(Material.STICK) { it is ItemTurn },
                    "mob" to Pair(Material.CREEPER_SPAWN_EGG) { it is MobTurn },
                    "throwable" to Pair(Material.EGG) { it is ThrowableTurn },
                    "structure" to Pair(Material.BAMBOO_BLOCK) { it is StructureTurn },
                    "glass-wall" to Pair(Material.PURPLE_STAINED_GLASS) { it is GlassWallTurn },
                    "other" to Pair(Material.STRUCTURE_BLOCK) { it is OtherTurn },
                ), "usage")
            })
            .addIngredient('2', Items.ClickItem(filter(Material.LAVA_BUCKET, "class")) { _, _ ->
                filterUsageMenu(mapOf(
                    "neutral" to Pair(Material.DIRT) { it.clazz == TurnClazz.NEUTRAL },
                    "hot" to Pair(Material.MAGMA_BLOCK) { it.clazz == TurnClazz.HOT },
                    "cold" to Pair(Material.BLUE_ICE) { it.clazz == TurnClazz.COLD },
                    "water" to Pair(Material.WATER_BUCKET) { it.clazz == TurnClazz.WATER },
                    "nature" to Pair(Material.AZALEA_LEAVES) { it.clazz == TurnClazz.NATURE },
                    "redstone" to Pair(Material.REDSTONE_BLOCK) { it.clazz == TurnClazz.REDSTONE },
                    "supernatural" to Pair(Material.REPEATING_COMMAND_BLOCK) { it.clazz == TurnClazz.SUPERNATURAL },
                ), "class")
            })
            .addIngredient('3', Items.ClickItem(filter(Material.IRON_AXE, "function")) { _, _ ->
                filterUsageMenu(mapOf(
                    "attack" to Pair(Material.DIAMOND_SWORD) { it is AttackFunction },
                    "buff" to Pair(Material.SPLASH_POTION) { it is BuffFunction },
                    "counterattack" to Pair(Material.DIAMOND_CHESTPLATE) { it is CounterattackFunction },
                    "counterbuff" to Pair(Material.WITHER_ROSE) { it is CounterbuffFunction },
                    "counter" to Pair(Material.TOTEM_OF_UNDYING) { it is CounterFunction },
                    "defense" to Pair(Material.SHIELD) { it is DefenseFunction },
                    "event" to Pair(Material.FIREWORK_ROCKET) { it is EventFunction },
                    "warp" to Pair(Material.END_PORTAL_FRAME) { it is WarpFunction },
                ), "function")
            })

            .addIngredient('$', Items.UpdatingClickItem({ ItemBuilder(if (onlyAffordable) Material.GOLD_INGOT else Material.COPPER_INGOT).apply {
                name(locale.component("shop.affordable", color = if (onlyAffordable) Colors.ACCENT else Colors.SECONDARY))
                addLore(locale.component("shop.affordable.status", color = Colors.EXTRA).appendSpace().append(locale.component("shop.affordable.status.${if (onlyAffordable) "" else "in"}active", color = if (onlyAffordable) Colors.POSITIVE else Colors.NEGATIVE)))
                addLore(locale.component("shop.affordable.toggle", color = Colors.EXTRA))
            }.build()
            }) { _, _ ->
                onlyAffordable = !onlyAffordable
                syncContent()
            })

            .addIngredient('D', Items.ClickItem(
                ItemBuilder(Material.LIME_DYE).apply {
                    name(locale.component("shop.done.name", color = Colors.ACCENT, decoration = TextDecoration.BOLD))
                    addLore(locale.component("shop.done.lore.1", color = Colors.NEUTRAL))
                    addLore(locale.component("shop.done.lore.2", color = Colors.NEUTRAL))
                }.build()
            ) { _, _ -> player.closeInventory() })

            .addIngredient('^', Items.ScrollItem(true))
            .addIngredient('v', Items.ScrollItem(false))

            .addIngredient('*', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .build()
    }

    override fun open() {
        syncContent()
        Window.single().setTitle("=== Shop ===").addCloseHandler {
            bukkitRun {
                if (player.player.openInventory.topInventory.type != InventoryType.CHEST)
                    done()
            }
        }.setGui(gui).open(player.player)
    }

    override fun give(turn: Turn<*>): Boolean {
        if (player.inventory.removeCoins(turn.cost ?: 0)) {
            player.inventory.add(turn)
            syncContent()
            return true
        }
        player.playSound(Sound.ENTITY_VILLAGER_NO)
        return false
    }

    fun syncContent() = gui.setContent(content { turn -> filter.values.all { it.second(turn) } })

    private fun filterUsageMenu(filters: Map<String, Pair<Material, (Turn<*>) -> Boolean>>, category: String) {
        val locale = player.locale()

        val gui = Gui.normal().setStructure(
            "0 1 2 3 4 5 6 7 8", // 0-8 = Filter Names
            "a b c d e f g h i", // a-i = States/Flags
            "# # # # D # # # #") // D = Done Button
            .setBackground(Items.GRAY_BACKGROUND)
            .addIngredient('#', Items.GRAY_BACKGROUND)
            .addIngredient(
                'D', Items.ClickItem(ItemBuilder(Material.LIME_DYE)
                    .name(locale.component("shop.done.name", color = Colors.ACCENT, decoration = TextDecoration.BOLD))
                    .build()
                ) { _, _ ->
                    player.closeInventory()
                    open()
                })

        val filterList: List<Map.Entry<String, Pair<Material, (Turn<*>) -> Boolean>>> = filters.entries.toList()
        val offset = (9 - filterList.size) / 2
        for (i in filterList.indices) {
            try {
                val f = filterList[i]
                gui.addIngredient(('0'.code + i + offset).toChar(), ItemBuilder(f.value.first).name(locale.component("info.$category.${f.key}", color = Colors.ACCENT)).build())
                gui.addIngredient(('a'.code + i + offset).toChar(), state(category + "." + f.key, f.value.second, filters))
            } catch (e: Exception) {
                NeoBB.LOG.warn("${player.game.id} - \"${player.name()}\": Could not add filter to inventory: ${e.message}")
            }
        }

        Window.single().setTitle("=== Filter ===").addCloseHandler {
            player.closeInventory()
            open()
        }.setGui(gui.build()).open(player.player)
    }

    private fun state(name: String, check: (Turn<*>) -> Boolean, f: Map<String, Pair<Material, (Turn<*>) -> Boolean>>): Items.ClickItem {
        val state = (if (filter.containsKey(name)) filter[name]?.first else FilterState.UNSET) ?: FilterState.UNSET
        return Items.ClickItem(ItemBuilder(state.item)
                .name(player.locale().component("shop.filter.${state.name.lowercase()}", color = state.color))
                .addLore(player.locale().component("shop.filter.lore-${state.name.lowercase()}"))
                .build()
        ) { _, e ->
            if (e.isLeftClick && state !== FilterState.YES) {
                filter[name] = Pair(FilterState.YES, check)
            } else if (e.isRightClick && state !== FilterState.NO) {
                filter[name] = Pair(FilterState.NO) { !check(it) }
            } else {
                filter.remove(name)
            }
            player.closeInventory()
            filterUsageMenu(f, name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
        }
    }

    private fun filter(item: Material, name: String): ItemStack {
        return ItemBuilder(item)
            .name(player.locale().component("shop.filter", color = Colors.ACCENT)
                .append(component(" - ", Colors.EXTRA))
                .append(player.locale().component("info.$name.$name", color = Colors.SECONDARY)))
            .build()
    }
}
