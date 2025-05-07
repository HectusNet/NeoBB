package net.hectus.neobb.shop

import com.marcpg.libpg.util.ItemBuilder
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.mode.LegacyGame
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.shop.util.FilterState
import net.hectus.neobb.shop.util.Items
import net.hectus.neobb.turn.ComboTurn
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import net.hectus.neobb.turn.default_game.attribute.function.*
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.default_game.flower.FlowerTurn
import net.hectus.neobb.turn.default_game.item.ItemTurn
import net.hectus.neobb.turn.default_game.mob.MobTurn
import net.hectus.neobb.turn.default_game.other.OtherTurn
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.turn.default_game.structure.glass_wall.GlassWallTurn
import net.hectus.neobb.turn.default_game.throwable.ThrowableTurn
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.asString
import net.hectus.neobb.util.bukkitRun
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window
import java.util.stream.Collectors
import kotlin.reflect.KClass

class DefaultShop(player: NeoPlayer) : Shop(player) {
    private val buyConsumer: (Map<ItemStack, Turn<*>>, ItemStack) -> Boolean = fun(m, i): Boolean {
        val turn = turn(m, i.type)
        if (player.inventory.removeCoins(turn.cost)) {
            if (player.inventory.allows(turn, i.type)) {
                player.inventory.add(i, turn)
                return true
            }
            player.inventory.addCoins(turn.cost)
        }
        player.playSound(Sound.ENTITY_VILLAGER_NO)
        return false
    }

    private val filter: MutableMap<String, Pair<FilterState, (Turn<*>) -> Boolean>> = hashMapOf()
    private val gui: ScrollGui<Item>

    init {
        val locale = player.locale()

        gui = if (player.game is LegacyGame) {
            ScrollGui.items().setStructure(
                "1 # # # # # # # ^", // # = Border
                "2 # * * * * * * #", // F = Filters
                "3 # * * * * * * #", // * = Items
                "4 # * * * * * * #", // D = Done Button
                "5 # * * * * * * #", // ^ = Scroll Up
                "6 # # # D # # # v") // v = Scroll Down
                .setBackground(Items.BLACK_BACKGROUND)
                .addIngredient('#', Items.GRAY_BACKGROUND)

                .addIngredient('1', Items.ClickItem(filter(Material.EGG, "usage")) { _, _ ->
                    filterUsageMenu(mapOf(
                        "block" to Pair(Material.GRASS_BLOCK, BlockTurn::class),
                        "item" to Pair(Material.STICK, ItemTurn::class),
                        "mob" to Pair(Material.CREEPER_SPAWN_EGG, MobTurn::class),
                        "throwable" to Pair(Material.EGG, ThrowableTurn::class),
                        "structure" to Pair(Material.BAMBOO_BLOCK, StructureTurn::class)
                    ), "usage")
                })
                .addIngredient('2', Items.ClickItem(filter(Material.LAVA_BUCKET, "class")) { _, _ ->
                    filterUsageMenu(mapOf(
                        "neutral" to Pair(Material.DIRT, NeutralClazz::class),
                        "hot" to Pair(Material.MAGMA_BLOCK, HotClazz::class),
                        "cold" to Pair(Material.BLUE_ICE, ColdClazz::class),
                        "water" to Pair(Material.WATER_BUCKET, WaterClazz::class),
                        "nature" to Pair(Material.AZALEA_LEAVES, NatureClazz::class),
                        "redstone" to Pair(Material.REDSTONE_BLOCK, RedstoneClazz::class),
                        "dream" to Pair(Material.WHITE_WOOL, SupernaturalClazz::class)
                    ), "clazz")
                })
                .addIngredient('3', Items.ClickItem(filter(Material.IRON_AXE, "type")) { _, _ ->
                    filterUsageMenu(mapOf(
                        "attack" to Pair(Material.DIAMOND_SWORD, AttackFunction::class),
                        "counter" to Pair(Material.TOTEM_OF_UNDYING, CounterFunction::class),
                        "counterattack" to Pair(Material.DIAMOND_CHESTPLATE, CounterattackFunction::class),
                        "warp" to Pair(Material.END_PORTAL_FRAME, WarpFunction::class),
                        "buff" to Pair(Material.SPLASH_POTION, BuffFunction::class),
                        "defense" to Pair(Material.SHIELD, DefenseFunction::class),
                        "await" to Pair(Material.CLOCK, ComboTurn::class)
                    ), "class")
                })

                .addIngredient('D', Items.ClickItem(ItemBuilder(Material.LIME_DYE)
                    .name(locale.component("shop.done.name", color = Colors.ACCENT, decoration = TextDecoration.BOLD))
                    .addLore(locale.component("shop.done.lore.1", color = Colors.NEUTRAL))
                    .addLore(locale.component("shop.done.lore.2", color = Colors.NEUTRAL))
                    .build()) { _, _ -> player.closeInv() })

                .addIngredient('^', Items.ScrollItem(true))
                .addIngredient('v', Items.ScrollItem(false))

                .addIngredient('*', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .build()
        } else {
            ScrollGui.items().setStructure(
                "1 # # # # # # # ^", // # = Border
                "2 # * * * * * * #", // F = Filters
                "3 # * * * * * * #", // * = Items
                "4 # * * * * * * #", // D = Done Button
                "5 # * * * * * * #", // ^ = Scroll Up
                "6 # # # D # # # v") // v = Scroll Down
                .setBackground(Items.BLACK_BACKGROUND)
                .addIngredient('#', Items.GRAY_BACKGROUND)

                .addIngredient('1', Items.ClickItem(filter(Material.EGG, "usage")) { _, _ ->
                    filterUsageMenu(mapOf(
                        "block" to Pair(Material.GRASS_BLOCK, BlockTurn::class),
                        "flower" to Pair(Material.POPPY, FlowerTurn::class),
                        "item" to Pair(Material.STICK, ItemTurn::class),
                        "mob" to Pair(Material.CREEPER_SPAWN_EGG, MobTurn::class),
                        "throwable" to Pair(Material.EGG, ThrowableTurn::class),
                        "structure" to Pair(Material.BAMBOO_BLOCK, StructureTurn::class),
                        "glass-wall" to Pair(Material.PURPLE_STAINED_GLASS, GlassWallTurn::class),
                        "other" to Pair(Material.STRUCTURE_BLOCK, OtherTurn::class)
                    ), "usage")
                })
                .addIngredient('2', Items.ClickItem(filter(Material.LAVA_BUCKET, "class")) { _, _ ->
                    filterUsageMenu(mapOf(
                        "neutral" to Pair(Material.DIRT, NeutralClazz::class),
                        "hot" to Pair(Material.MAGMA_BLOCK, HotClazz::class),
                        "cold" to Pair(Material.BLUE_ICE, ColdClazz::class),
                        "water" to Pair(Material.WATER_BUCKET, WaterClazz::class),
                        "nature" to Pair(Material.AZALEA_LEAVES, NatureClazz::class),
                        "redstone" to Pair(Material.REDSTONE_BLOCK, RedstoneClazz::class),
                        "supernatural" to Pair(Material.REPEATING_COMMAND_BLOCK, SupernaturalClazz::class)
                    ), "clazz")
                })
                .addIngredient('3', Items.ClickItem(filter(Material.IRON_AXE, "function")) { _, _ ->
                    filterUsageMenu(mapOf(
                        "attack" to Pair(Material.DIAMOND_SWORD, AttackFunction::class),
                        "buff" to Pair(Material.SPLASH_POTION, BuffFunction::class),
                        "counterattack" to Pair(Material.DIAMOND_CHESTPLATE, CounterattackFunction::class),
                        "counterbuff" to Pair(Material.WITHER_ROSE, CounterbuffFunction::class),
                        "counter" to Pair(Material.TOTEM_OF_UNDYING, CounterFunction::class),
                        "defense" to Pair(Material.SHIELD, DefenseFunction::class),
                        "event" to Pair(Material.FIREWORK_ROCKET, EventFunction::class),
                        "warp" to Pair(Material.END_PORTAL_FRAME, WarpFunction::class)
                    ), "class")
                })

                .addIngredient('D', Items.ClickItem(
                    ItemBuilder(Material.LIME_DYE)
                        .name(locale.component("shop.done.name", color = Colors.ACCENT, decoration = TextDecoration.BOLD))
                        .addLore(locale.component("shop.done.lore.1", color = Colors.NEUTRAL))
                        .addLore(locale.component("shop.done.lore.2", color = Colors.NEUTRAL))
                        .build()
                ) { _, _ -> player.closeInv() })

                .addIngredient('^', Items.ScrollItem(true))
                .addIngredient('v', Items.ScrollItem(false))

                .addIngredient('*', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .build()
        }
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

    private fun syncContent() {
        gui.setContent(content(dummyTurns.stream()
                    .filter { t -> filter.values.all { it.second.invoke(t) } }
                    .flatMap { t -> t.items().stream().map { i -> Pair(ItemBuilder(i).lore(loreBuilder.turn(t).buildWithTooltips(player.locale())).build(), t) } }
                    .sorted(Comparator.comparing { it.first.displayName().asString() })
                    .collect(Collectors.toMap({ it.first }, { it.second }, { e, _ -> e }, { linkedMapOf() }))
        ))
    }

    private fun filterUsageMenu(filters: Map<String, Pair<Material, KClass<*>>>, category: String) {
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
                    player.closeInv()
                    open()
                })

        val filterList: List<Map.Entry<String, Pair<Material, KClass<*>>>> = ArrayList(filters.entries)
        val offset = (9 - filterList.size) / 2
        println("Filter Slot Offset: $offset")
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
            player.closeInv()
            open()
        }.setGui(gui.build()).open(player.player)
    }

    private fun state(name: String, superClass: KClass<*>, f: Map<String, Pair<Material, KClass<*>>>): Items.ClickItem {
        val state = (if (filter.containsKey(name)) filter[name]?.first else FilterState.UNSET) ?: FilterState.UNSET
        return Items.ClickItem(ItemBuilder(state.item)
                .name(player.locale().component("shop.filter." + state.name.lowercase(), color = state.color))
                .addLore(player.locale().component("shop.filter.lore-" + state.name.lowercase()))
                .build()
        ) { _, e ->
            if (e.isLeftClick && state !== FilterState.YES) {
                filter[name] = Pair(FilterState.YES) { superClass.isInstance(it) }
            } else if (e.isRightClick && state !== FilterState.NO) {
                filter[name] = Pair(FilterState.NO) { !superClass.isInstance(it) }
            } else {
                filter.remove(name)
            }
            player.closeInv()
            filterUsageMenu(f, name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
        }
    }

    private fun content(items: LinkedHashMap<ItemStack, Turn<*>>): List<Item> {
        return items.sequencedKeySet().map { i -> Items.ClickItem(i) { _, e -> when (e.click) {
            ClickType.LEFT -> buyConsumer.invoke(items, i)
            ClickType.RIGHT -> repeat(3) {
                if (!buyConsumer.invoke(items, i))
                    return@ClickItem
            }
            ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> repeat(player.game.info.deckSize) {
                if (!buyConsumer.invoke(items, i))
                    return@ClickItem
            }
            else -> e.isCancelled = true
        } } }
    }

    private fun filter(item: Material, name: String): ItemStack {
        return ItemBuilder(item)
            .name(player.locale().component("shop.filter", color = Colors.ACCENT)
                .append(Component.text(" - ", Colors.EXTRA))
                .append(player.locale().component("info.$name.$name", color = Colors.SECONDARY)))
            .build()
    }

    private fun turn(map: Map<ItemStack, Turn<*>>, material: Material): Turn<*> {
        for ((key, value) in map) {
            if (key.type == material) return value
        }
        return Turn.DUMMY
    }
}
