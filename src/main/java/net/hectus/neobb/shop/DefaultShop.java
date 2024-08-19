package net.hectus.neobb.shop;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.*;
import net.hectus.neobb.turn.default_game.attributes.usage.*;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.ItemBuilder;
import net.hectus.neobb.util.ItemLoreBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultShop extends Shop {
    public DefaultShop(Game game) {
        super(game);
    }

    @Override
    public void open(NeoPlayer player) {
        menu(player);
    }

    public void menu(@NotNull NeoPlayer player) {
        Locale l = player.locale();

        ChestGui gui = new ChestGui(6, Translation.string(l, "item-lore.cost.value", player.inventory.coins()) + " - Shop - Menu");
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnClose(event -> {
            if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) return;
            Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> menu(player), 1);
        });

        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        gui.addPane(background);

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(new ItemBuilder(Material.LIME_DYE)
                .name(Translation.component(l, "shop.done.name").color(Colors.ACCENT).decorate(TextDecoration.BOLD))
                .addLore(Translation.component(l, "shop.done.lore.1").color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, false))
                .addLore(Translation.component(l, "shop.done.lore.2").color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, false))
                .build(), event -> {
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
            done(player);
        }), 4, 0);
        gui.addPane(navigation);

        StaticPane filters = new StaticPane(1, 5);
        filters.addItem(filterCategory(l, Material.EGG, "usage"), 0, 0);
        filters.addItem(filterCategory(l, Material.IRON_AXE, "function"), 0, 2);
        filters.addItem(filterCategory(l, Material.LAVA_BUCKET, "class"), 0, 4);
        gui.addPane(filters);

        StaticPane typeFilters = new StaticPane(2, 0, 7, 1);
        typeFilters.addItem(filter(l, Material.BLUE_CONCRETE, "usage", "block", player, t -> t instanceof BlockUsage), 0, 0);
        typeFilters.addItem(filter(l, Material.RED_DYE, "usage", "item", player, t -> t instanceof ItemUsage), 1, 0);
        typeFilters.addItem(filter(l, Material.FIRE_CHARGE, "usage", "throwable", player, t -> t instanceof ThrowableUsage), 2, 0);
        typeFilters.addItem(filter(l, Material.CREEPER_SPAWN_EGG, "usage", "mob", player, t -> t instanceof MobUsage), 3, 0);
        typeFilters.addItem(filter(l, Material.BAMBOO_BLOCK, "usage", "structure", player, t -> t instanceof StructureUsage), 4, 0);
        typeFilters.addItem(filter(l, Material.END_PORTAL_FRAME, "usage", "warp", player, t -> t instanceof WarpFunction), 6, 0);
        typeFilters.addItem(filter(l, Material.STRUCTURE_BLOCK, "usage", "other", player, t -> t instanceof OtherUsage<?>), 5, 0);
        gui.addPane(typeFilters);

        StaticPane functionFilters = new StaticPane(2, 2, 7, 1);
        functionFilters.addItem(filter(l, Material.DIAMOND_SWORD, "function", "attack", player, t -> t instanceof AttackFunction), 0, 0);
        functionFilters.addItem(filter(l, Material.TOTEM_OF_UNDYING, "function", "counter", player, t -> t instanceof CounterFunction), 1, 0);
        functionFilters.addItem(filter(l, Material.DIAMOND_CHESTPLATE, "function", "counterattack", player, t -> t instanceof CounterFunction && t instanceof AttackFunction), 2, 0);
        functionFilters.addItem(filter(l, Material.WITHER_ROSE, "function", "counterbuff", player, t -> t instanceof CounterFunction && t instanceof BuffFunction), 3, 0);
        functionFilters.addItem(filter(l, Material.SPLASH_POTION, "function", "buff", player, t -> t instanceof BuffFunction), 4, 0);
        functionFilters.addItem(filter(l, Material.SHIELD, "function", "defense", player, t -> t instanceof DefenseFunction), 5, 0);
        functionFilters.addItem(filter(l, Material.FIREWORK_ROCKET, "function", "event", player, t -> t instanceof EventFunction), 6, 0);
        gui.addPane(functionFilters);

        StaticPane classFilters = new StaticPane(2, 4, 7, 1);
        classFilters.addItem(filter(l, Material.DIRT, "class", "neutral", player, t -> t instanceof NeutralClazz), 0, 0);
        classFilters.addItem(filter(l, Material.MAGMA_BLOCK, "class", "hot", player, t -> t instanceof HotClazz), 1, 0);
        classFilters.addItem(filter(l, Material.BLUE_ICE, "class", "cold", player, t -> t instanceof ColdClazz), 2, 0);
        classFilters.addItem(filter(l, Material.WATER_BUCKET, "class", "water", player, t -> t instanceof WaterClazz), 3, 0);
        classFilters.addItem(filter(l, Material.AZALEA_LEAVES, "class", "nature", player, t -> t instanceof NatureClazz), 4, 0);
        classFilters.addItem(filter(l, Material.REDSTONE_BLOCK, "class", "redstone", player, t -> t instanceof RedstoneClazz), 5, 0);
        classFilters.addItem(filter(l, Material.REPEATING_COMMAND_BLOCK, "class", "supernatural", player, t -> t instanceof SupernaturalClazz), 6, 0);
        gui.addPane(classFilters);

        gui.show(player.player);
    }

    private @NotNull GuiItem filterCategory(Locale l, Material material, String category) {
        return new GuiItem(new ItemBuilder(material)
                .name(Translation.component(l, "shop.filters").color(Colors.ACCENT)
                        .append(Component.text(" - ", Colors.EXTRA))
                        .append(Translation.component(l, "info." + category + "." + category).color(Colors.NEUTRAL)))
                .build()
        );
    }

    private @NotNull GuiItem filter(Locale l, Material material, String category, String filter, NeoPlayer player, Predicate<Turn<?>> filterLogic) {
        return new GuiItem(new ItemBuilder(material)
                .name(Translation.component(l, "info." + category + "." + category).color(Colors.ACCENT)
                        .append(Component.text(" - ", Colors.EXTRA))
                        .append(Translation.component(l, "info." + category + "." + filter).color(Colors.NEUTRAL)))
                .build(),
                event -> shop(player, Translation.string(l, "info." + category + "." + category), filterLogic)
        );
    }

    public void shop(@NotNull NeoPlayer player, String category, Predicate<Turn<?>> categoryFilter) {
        Locale l = player.locale();

        Map<ItemStack, Turn<?>> turnItems = turns.stream()
                .map(clazz -> turn(clazz, player))
                .filter(categoryFilter)
                .map(turn -> Pair.of(new ItemBuilder(turn.item()).lore(new ItemLoreBuilder(turn).build(l)).build(), turn))
                .collect(Collectors.toMap(Pair::left, Pair::right));

        ChestGui gui = new ChestGui(6, Translation.string(l, "item-lore.cost.value", player.inventory.coins()) + " - Shop - " + category);
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnClose(event -> {
            if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) return;
            Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> menu(player), 1);
        });

        gui.setOnBottomClick(event -> {
            if (event.getSlotType() == InventoryType.SlotType.QUICKBAR && event.getCurrentItem() != null && !event.getCurrentItem().isEmpty() && event.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE) {
                player.inventory.setDeckSlot(event.getSlot(), null, null);
                player.inventory.addCoins(turn(turnItems, event.getCurrentItem().getType()).cost());
            }
        });

        PaginatedPane items = new PaginatedPane(9, 5);
        items.populateWithItemStacks(new ArrayList<>(turnItems.keySet()));
        items.setOnClick(event -> {
            ItemStack item = event.getCurrentItem();
            if (item == null || item.isEmpty()) return;
            if (player.inventory.removeCoins(cost(turnItems, item.getType()))) {
                try {
                    Turn<?> turn = turn(turnItems, item.getType());
                    if (!player.inventory.allowItem(turn, item.getType()))
                        throw new IndexOutOfBoundsException();
                    player.inventory.addToDeck(item, turn);
                    gui.setTitle(Translation.string(l, "item-lore.cost.value", player.inventory.coins()) + " - Shop - " + category);
                } catch (IndexOutOfBoundsException e) {
                    player.player.playSound(player.player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    player.inventory.addCoins(cost(turnItems, item.getType()));
                }
            } else {
                player.player.playSound(player.player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        });
        gui.addPane(items);

        OutlinePane background = new OutlinePane(0, 5, 9, 1, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        gui.addPane(background);

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        // Previous Page
        navigation.addItem(new GuiItem(new ItemBuilder(Material.PLAYER_HEAD)
                .editMeta(meta -> ((SkullMeta) meta).setOwningPlayer(Bukkit.getOfflinePlayer("MHF_ArrowLeft")))
                .name(Translation.component(l, "shop.previous").color(Colors.NEUTRAL))
                .build(), event -> {
            if (items.getPage() > 0) {
                items.setPage(items.getPage() - 1);
                gui.update();
            }
        }), 0, 0);
        // Done
        navigation.addItem(new GuiItem(new ItemBuilder(Material.GRAY_DYE)
                .name(Translation.component(l, "shop.menu.name").color(Colors.ACCENT).decorate(TextDecoration.BOLD))
                .addLore(Translation.component(l, "shop.menu.lore").color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, false))
                .build(), event -> event.getWhoClicked().closeInventory()), 4, 0);
        // Next Page
        navigation.addItem(new GuiItem(new ItemBuilder(Material.PLAYER_HEAD)
                .editMeta(meta -> ((SkullMeta) meta).setOwningPlayer(Bukkit.getOfflinePlayer("MHF_ArrowRight")))
                .name(Translation.component(l, "shop.next").color(Colors.POSITIVE))
                .build(), event -> {
            if (items.getPage() < items.getPages() - 1) {
                items.setPage(items.getPage() + 1);
                gui.update();
            }
        }), 8, 0);
        gui.addPane(navigation);

        gui.show(player.player);
    }

    private static int cost(@NotNull Map<ItemStack, Turn<?>> map, Material material) {
        for (Map.Entry<ItemStack, Turn<?>> entry : map.entrySet()) {
            if (entry.getKey().getType() == material) return entry.getValue().cost();
        }
        return 10; // Fallback to the high value of 10, to make sure you cannot exploit it.
    }

    private static Turn<?> turn(@NotNull Map<ItemStack, Turn<?>> map, Material material) {
        for (Map.Entry<ItemStack, Turn<?>> entry : map.entrySet()) {
            if (entry.getKey().getType() == material) return entry.getValue();
        }
        return Turn.DUMMY;
    }
}
