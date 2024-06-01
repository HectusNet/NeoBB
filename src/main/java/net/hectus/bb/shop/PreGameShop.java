package net.hectus.bb.shop;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.BlockBattles;
import net.hectus.bb.player.PlayerData;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.util.ItemBuilder;
import net.hectus.bb.util.ItemLoreBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Predicate;

public final class PreGameShop {
    //
    // I am very very sorry!
    //
    // I'm sorry for making this boilerplate shit code.
    // I shall consider ending my life :(
    //
    // Nah but fr, i'm sorry for making this boilerplate code that can probably be way more efficient!
    //

    // ========== SHOP FILTER SELECTION MENU ==========

    private static final Component HYPHEN = Component.text(" - ", NamedTextColor.DARK_GRAY);

    public static void menu(@NotNull PlayerData player) {
        Locale l = player.player().locale();

        ChestGui gui = new ChestGui(6, "Shop - Menu");
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnClose(event -> {
            if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) return;
            Bukkit.getScheduler().runTaskLater(BlockBattles.getPlugin(BlockBattles.class), () -> menu(player), 1);
        });

        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        gui.addPane(background);

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(new ItemBuilder(Material.LIME_DYE)
                .name(Translation.component(l, "shop.done.name").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD))
                .addLore(Translation.component(l, "shop.done.lore.1").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                .addLore(Translation.component(l, "shop.done.lore.2").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                .build(), event -> {
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
            player.inv().currentDone();
        }), 4, 0);
        gui.addPane(navigation);

        StaticPane filters = new StaticPane(1, 5);
        filters.addItem(filterCategory(l, Material.EGG, "type"), 0, 0);
        filters.addItem(filterCategory(l, Material.IRON_AXE, "function"), 0, 2);
        filters.addItem(filterCategory(l, Material.LAVA_BUCKET, "class"), 0, 4);
        gui.addPane(filters);

        StaticPane typeFilters = new StaticPane(2, 0, 7, 1);
        typeFilters.addItem(filter(l, Material.BLUE_CONCRETE, "type", "block", player, t -> t.type == Turn.ItemType.BLOCK), 0, 0);
        typeFilters.addItem(filter(l, Material.RED_DYE, "type", "item", player, t -> t.type == Turn.ItemType.ITEM), 1, 0);
        typeFilters.addItem(filter(l, Material.FIRE_CHARGE, "type", "throwable", player, t -> t.type == Turn.ItemType.THROWABLE), 2, 0);
        typeFilters.addItem(filter(l, Material.CREEPER_SPAWN_EGG, "type", "mob", player, t -> t.type == Turn.ItemType.MOB), 3, 0);
        typeFilters.addItem(filter(l, Material.BAMBOO_BLOCK, "type", "structure", player, t -> t.type == Turn.ItemType.STRUCTURE), 4, 0);
        typeFilters.addItem(filter(l, Material.STRUCTURE_BLOCK, "type", "other", player, t -> t.type == Turn.ItemType.OTHER), 5, 0);
        gui.addPane(typeFilters);

        StaticPane functionFilters = new StaticPane(2, 2, 7, 1);
        functionFilters.addItem(filter(l, Material.DIAMOND_SWORD, "function", "attack", player, t -> t.function == Turn.ItemFunction.ATTACK), 0, 0);
        functionFilters.addItem(filter(l, Material.TOTEM_OF_UNDYING, "function", "counter", player, t -> t.function == Turn.ItemFunction.COUNTER), 1, 0);
        functionFilters.addItem(filter(l, Material.DIAMOND_CHESTPLATE, "function", "counterattack", player, t -> t.function == Turn.ItemFunction.COUNTERATTACK), 2, 0);
        functionFilters.addItem(filter(l, Material.WITHER_ROSE, "function", "counterbuff", player, t -> t.function == Turn.ItemFunction.COUNTERBUFF), 3, 0);
        functionFilters.addItem(filter(l, Material.SPLASH_POTION, "function", "buff", player, t -> t.function == Turn.ItemFunction.BUFF), 4, 0);
        functionFilters.addItem(filter(l, Material.SHIELD, "function", "defense", player, t -> t.function == Turn.ItemFunction.DEFENSE), 5, 0);
        functionFilters.addItem(filter(l, Material.FIREWORK_ROCKET, "function", "event", player, t -> t.function == Turn.ItemFunction.EVENT), 6, 0);
        gui.addPane(functionFilters);

        StaticPane classFilters = new StaticPane(2, 4, 7, 1);
        classFilters.addItem(filter(l, Material.DIRT, "class", "neutral", player, t -> t.clazz == Turn.ItemClass.NEUTRAL), 0, 0);
        classFilters.addItem(filter(l, Material.MAGMA_BLOCK, "class", "hot", player, t -> t.clazz == Turn.ItemClass.HOT), 1, 0);
        classFilters.addItem(filter(l, Material.BLUE_ICE, "class", "cold", player, t -> t.clazz == Turn.ItemClass.COLD), 2, 0);
        classFilters.addItem(filter(l, Material.WATER_BUCKET, "class", "water", player, t -> t.clazz == Turn.ItemClass.WATER_CLASS), 3, 0);
        classFilters.addItem(filter(l, Material.AZALEA_LEAVES, "class", "nature", player, t -> t.clazz == Turn.ItemClass.NATURE), 4, 0);
        classFilters.addItem(filter(l, Material.REDSTONE_BLOCK, "class", "redstone", player, t -> t.clazz == Turn.ItemClass.REDSTONE), 5, 0);
        classFilters.addItem(filter(l, Material.REPEATING_COMMAND_BLOCK, "class", "supernatural", player, t -> t.clazz == Turn.ItemClass.SUPERNATURAL), 6, 0);
        gui.addPane(classFilters);

        gui.show(player.player());
    }

    private static @NotNull GuiItem filterCategory(Locale l, Material material, String category) {
        return new GuiItem(new ItemBuilder(material)
                .name(Translation.component(l, "shop.filters").color(NamedTextColor.BLUE)
                        .append(HYPHEN)
                        .append(Translation.component(l, "info." + category + "." + category).color(NamedTextColor.YELLOW)))
                .build()
        );
    }

    private static @NotNull GuiItem filter(Locale l, Material material, String category, String filter, PlayerData player, Predicate<Turn> filterLogic) {
        return new GuiItem(new ItemBuilder(material)
                .name(Translation.component(l, "info." + category + "." + category).color(NamedTextColor.BLUE)
                        .append(HYPHEN)
                        .append(Translation.component(l, "info." + category + "." + filter).color(NamedTextColor.YELLOW)))
                .build(),
                event -> shop(player, Translation.string(l, "info." + category + "." + category), filterLogic)
        );
    }

    // ========== FILTERED SHOP PAGES ==========

    public static void shop(@NotNull PlayerData player, String category, Predicate<Turn> categoryFilter) {
        Locale l = player.player().locale();

        ChestGui gui = new ChestGui(6, Translation.string(l, "item-lore.cost.value", player.inv().coins()) + " - Shop - " + category);
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnClose(event -> {
            if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) return;
            Bukkit.getScheduler().runTaskLater(BlockBattles.getPlugin(BlockBattles.class), () -> menu(player), 1);
        });

        PaginatedPane items = new PaginatedPane(9, 5);
        items.populateWithItemStacks(ShopItemUtilities.ITEM_STACKS.stream()
                .filter(item -> categoryFilter.test(ShopItemUtilities.getTurn(item)))
                .map(item -> new ItemBuilder(item).lore(ItemLoreBuilder.of(item).build(l)).build())
                .toList());
        items.setOnClick(event -> {
            ItemStack item = event.getCurrentItem();
            if (item == null || item.isEmpty()) return;
            if (player.inv().removeCoins(ShopItemUtilities.getPrice(item))) {
                try {
                    player.inv().addItem(item);
                } catch (IndexOutOfBoundsException e) {
                    player.player().playSound(player.player(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                }
            } else {
                player.player().playSound(player.player(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
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
                .name(Translation.component(l, "shop.previous").color(NamedTextColor.YELLOW))
                .build(), event -> {
            if (items.getPage() > 0) {
                items.setPage(items.getPage() - 1);
                gui.update();
            }
        }), 0, 0);
        // Done
        navigation.addItem(new GuiItem(new ItemBuilder(Material.GRAY_DYE)
                .name(Translation.component(l, "shop.menu.name").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD))
                .addLore(Translation.component(l, "shop.menu.lore").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                .build(),  event -> event.getWhoClicked().closeInventory()), 4, 0);
        // Next Page
        navigation.addItem(new GuiItem(new ItemBuilder(Material.PLAYER_HEAD)
                .editMeta(meta -> ((SkullMeta) meta).setOwningPlayer(Bukkit.getOfflinePlayer("MHF_ArrowRight")))
                .name(Translation.component(l, "shop.next").color(NamedTextColor.GREEN))
                .build(), event -> {
            if (items.getPage() < items.getPages() - 1) {
                items.setPage(items.getPage() + 1);
                gui.update();
            }
        }), 8, 0);
        gui.addPane(navigation);

        gui.show(player.player());
    }
}
