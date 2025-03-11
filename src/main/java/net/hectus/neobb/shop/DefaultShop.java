package net.hectus.neobb.shop;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Pair;
import com.marcpg.libpg.util.ItemBuilder;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.mode.LegacyGame;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.util.FilterState;
import net.hectus.neobb.shop.util.Items;
import net.hectus.neobb.turn.DummyTurn;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.*;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.default_game.flower.FlowerTurn;
import net.hectus.neobb.turn.default_game.item.ItemTurn;
import net.hectus.neobb.turn.default_game.mob.MobTurn;
import net.hectus.neobb.turn.default_game.other.OtherTurn;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.turn.default_game.structure.glass_wall.GlassWallTurn;
import net.hectus.neobb.turn.default_game.throwable.ThrowableTurn;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultShop extends Shop {
    private final BiConsumer<Map<ItemStack, Turn<?>>, ItemStack> buyConsumer = (it, i) -> {
        Turn<?> turn = turn(it, i.getType());
        if (player.inventory.removeCoins(turn.cost())) {
            try {
                if (player.inventory.allowItem(turn, i.getType())) {
                    player.inventory.addToDeck(i, turn);
                    return;
                }
            } catch (IndexOutOfBoundsException ignored) {}
            player.inventory.addCoins(turn.cost());
        }
        player.playSound(Sound.ENTITY_VILLAGER_NO, 1.0f);
    };

    private final Map<String, Pair<FilterState, Predicate<Turn<?>>>> filters = new HashMap<>();
    private final ScrollGui<Item> gui;

    public DefaultShop(@NotNull NeoPlayer player) {
        super(player);

        Locale l = player.locale();

        if (player.game instanceof LegacyGame) {
            gui = ScrollGui.items().setStructure(
                            "1 # # # # # # # ^", // # = Border
                            "2 # * * * * * * #", // F = Filters
                            "3 # * * * * * * #", // * = Items
                            "4 # * * * * * * #", // D = Done Button
                            "5 # * * * * * * #", // ^ = Scroll Up
                            "6 # # # D # # # v") // v = Scroll Down
                    .setBackground(Items.BLACK_BACKGROUND).addIngredient('#', Items.GRAY_BACKGROUND)

                    .addIngredient('1', new Items.ClickItem(filter(Material.EGG, "usage"), (p, e) -> filterUsageMenu(Map.of(
                            "block", Pair.of(Material.GRASS_BLOCK, BlockTurn.class),
                            "item", Pair.of(Material.STICK, ItemTurn.class),
                            "mob", Pair.of(Material.CREEPER_SPAWN_EGG, MobTurn.class),
                            "throwable", Pair.of(Material.EGG, ThrowableTurn.class),
                            "structure", Pair.of(Material.BAMBOO_BLOCK, StructureTurn.class)
                    ), "usage")))
                    .addIngredient('2', new Items.ClickItem(filter(Material.LAVA_BUCKET, "class"), (p, e) -> filterUsageMenu(Map.of(
                            "neutral", Pair.of(Material.DIRT, NeutralClazz.class),
                            "hot", Pair.of(Material.MAGMA_BLOCK, HotClazz.class),
                            "cold", Pair.of(Material.BLUE_ICE, ColdClazz.class),
                            "water", Pair.of(Material.WATER_BUCKET, WaterClazz.class),
                            "nature", Pair.of(Material.AZALEA_LEAVES, NatureClazz.class),
                            "redstone", Pair.of(Material.REDSTONE_BLOCK, RedstoneClazz.class),
                            "dream", Pair.of(Material.WHITE_WOOL, SupernaturalClazz.class)
                    ), "clazz")))
                    .addIngredient('3', new Items.ClickItem(filter(Material.IRON_AXE, "type"), (p, e) -> filterUsageMenu(Map.of(
                            "attack", Pair.of(Material.DIAMOND_SWORD, AttackFunction.class),
                            "counter", Pair.of(Material.TOTEM_OF_UNDYING, CounterFunction.class),
                            "counterattack", Pair.of(Material.DIAMOND_CHESTPLATE, CounterattackFunction.class),
                            "warp", Pair.of(Material.END_PORTAL_FRAME, WarpFunction.class),
                            "buff", Pair.of(Material.SPLASH_POTION, BuffFunction.class),
                            "defense", Pair.of(Material.SHIELD, DefenseFunction.class),
                            "await", Pair.of(Material.CLOCK, DummyTurn.class)
                    ), "class")))

                    .addIngredient('D', new Items.ClickItem(new ItemBuilder(Material.LIME_DYE)
                            .name(Translation.component(l, "shop.done.name").color(Colors.ACCENT).decorate(TextDecoration.BOLD))
                            .addLore(Translation.component(l, "shop.done.lore.1").color(Colors.NEUTRAL))
                            .addLore(Translation.component(l, "shop.done.lore.2").color(Colors.NEUTRAL))
                            .build(), (p, e) -> player.closeInv()))
                    .addIngredient('^', new Items.ScrollItem(true))
                    .addIngredient('v', new Items.ScrollItem(false))

                    .addIngredient('*', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                    .build();
        } else {
            gui = ScrollGui.items().setStructure(
                            "1 # # # # # # # ^", // # = Border
                            "2 # * * * * * * #", // F = Filters
                            "3 # * * * * * * #", // * = Items
                            "4 # * * * * * * #", // D = Done Button
                            "5 # * * * * * * #", // ^ = Scroll Up
                            "6 # # # D # # # v") // v = Scroll Down
                    .setBackground(Items.BLACK_BACKGROUND).addIngredient('#', Items.GRAY_BACKGROUND)

                    .addIngredient('1', new Items.ClickItem(filter(Material.EGG, "usage"), (p, e) -> Bukkit.getScheduler().runTask(NeoBB.PLUGIN, () -> filterUsageMenu(Map.of(
                            "block", Pair.of(Material.GRASS_BLOCK, BlockTurn.class),
                            "flower", Pair.of(Material.POPPY, FlowerTurn.class),
                            "item", Pair.of(Material.STICK, ItemTurn.class),
                            "mob", Pair.of(Material.CREEPER_SPAWN_EGG, MobTurn.class),
                            "throwable", Pair.of(Material.EGG, ThrowableTurn.class),
                            "structure", Pair.of(Material.BAMBOO_BLOCK, StructureTurn.class),
                            "glass-wall", Pair.of(Material.PURPLE_STAINED_GLASS, GlassWallTurn.class),
                            "other", Pair.of(Material.STRUCTURE_BLOCK, OtherTurn.class)
                    ), "usage"))))
                    .addIngredient('2', new Items.ClickItem(filter(Material.LAVA_BUCKET, "class"), (p, e) -> Bukkit.getScheduler().runTask(NeoBB.PLUGIN, () -> filterUsageMenu(Map.of(
                            "neutral", Pair.of(Material.DIRT, NeutralClazz.class),
                            "hot", Pair.of(Material.MAGMA_BLOCK, HotClazz.class),
                            "cold", Pair.of(Material.BLUE_ICE, ColdClazz.class),
                            "water", Pair.of(Material.WATER_BUCKET, WaterClazz.class),
                            "nature", Pair.of(Material.AZALEA_LEAVES, NatureClazz.class),
                            "redstone", Pair.of(Material.REDSTONE_BLOCK, RedstoneClazz.class),
                            "supernatural", Pair.of(Material.REPEATING_COMMAND_BLOCK, SupernaturalClazz.class)
                    ), "clazz"))))
                    .addIngredient('3', new Items.ClickItem(filter(Material.IRON_AXE, "function"), (p, e) -> Bukkit.getScheduler().runTask(NeoBB.PLUGIN, () -> filterUsageMenu(Map.of(
                            "attack", Pair.of(Material.DIAMOND_SWORD, AttackFunction.class),
                            "buff", Pair.of(Material.SPLASH_POTION, BuffFunction.class),
                            "counterattack", Pair.of(Material.DIAMOND_CHESTPLATE, CounterattackFunction.class),
                            "counterbuff", Pair.of(Material.WITHER_ROSE, CounterbuffFunction.class),
                            "counter", Pair.of(Material.TOTEM_OF_UNDYING, CounterFunction.class),
                            "defense", Pair.of(Material.SHIELD, DefenseFunction.class),
                            "event", Pair.of(Material.FIREWORK_ROCKET, EventFunction.class),
                            "warp", Pair.of(Material.END_PORTAL_FRAME, WarpFunction.class)
                    ), "class"))))

                    .addIngredient('D', new Items.ClickItem(new ItemBuilder(Material.LIME_DYE)
                            .name(Translation.component(l, "shop.done.name").color(Colors.ACCENT).decorate(TextDecoration.BOLD))
                            .addLore(Translation.component(l, "shop.done.lore.1").color(Colors.NEUTRAL))
                            .addLore(Translation.component(l, "shop.done.lore.2").color(Colors.NEUTRAL))
                            .build(), (p, e) -> player.closeInv()))
                    .addIngredient('^', new Items.ScrollItem(true))
                    .addIngredient('v', new Items.ScrollItem(false))

                    .addIngredient('*', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                    .build();
        }
    }

    @Override
    public void open() {
        syncContent();
        Window.single().setTitle("=== Shop ===").addCloseHandler(() -> Bukkit.getScheduler().runTask(NeoBB.PLUGIN, () -> {
            if (player.player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST) done();
        })).setGui(gui).open(player.player);
    }

    public void syncContent() {
        gui.setContent(content(dummyTurns.stream()
                .filter(t -> filters.values().stream().allMatch(p -> p.right().test(t)))
                .flatMap(t -> t.items().stream().map(i -> Pair.of(new ItemBuilder(i).lore(loreBuilder.turn(t).buildWithTooltips(player.locale())).build(), t)))
                .sorted(Comparator.comparing(p -> PlainTextComponentSerializer.plainText().serialize(p.left().displayName())))
                .collect(Collectors.toMap(Pair::left, Pair::right, (e, r) -> e, LinkedHashMap::new))));
    }

    public void filterUsageMenu(@NotNull Map<String, Pair<Material, Class<?>>> filters, String category) {
        Locale l = player.locale();

        Gui.Builder.Normal gui = Gui.normal().setStructure(
                        "0 1 2 3 4 5 6 7 8", // 0-8 = Filter Names
                        "a b c d e f g h i", // a-i = States/Flags
                        "# # # # D # # # #") // D = Done Button
                .setBackground(Items.GRAY_BACKGROUND).addIngredient('#', Items.GRAY_BACKGROUND)
                .addIngredient('D', new Items.ClickItem(new ItemBuilder(Material.LIME_DYE)
                        .name(Translation.component(l, "shop.done.name").color(Colors.ACCENT).decorate(TextDecoration.BOLD))
                        .build(), (p, e) -> {
                    player.closeInv();
                    open();
                }));

        List<Map.Entry<String, Pair<Material, Class<?>>>> filterList = new ArrayList<>(filters.entrySet());
        int offset = (9 - filterList.size()) / 2;
        for (int i = 0; i < filterList.size(); i++) {
            try {
                Map.Entry<String, Pair<Material, Class<?>>> f = filterList.get(i);
                gui.addIngredient((char) ('0' + i + offset), new ItemBuilder(f.getValue().left()).name(Translation.component(l, "info." + category + "." + f.getKey()).color(Colors.ACCENT)).build());
                gui.addIngredient((char) ('a' + i + offset), state(category + "." + f.getKey(), f.getValue().right(), filters));
            } catch (Exception e) {
                NeoBB.LOG.warn("{} - \"{}\": Could not add filter to inventory: {}", player.game.id, player.name(), e.getMessage());
            }
        }

        Window.single().setTitle("=== Filter ===").addCloseHandler(() -> {
            player.closeInv();
            open();
        }).setGui(gui.build()).open(player.player);
    }

    public List<Item> content(@NotNull LinkedHashMap<ItemStack, Turn<?>> items) {
        return items.sequencedKeySet().stream().map(i -> (Item) new Items.ClickItem(i, (p, e) -> buyConsumer.accept(items, i))).toList();
    }

    public @NotNull Items.ClickItem state(@NotNull String name, Class<?> superClass, Map<String, Pair<Material, Class<?>>> f) {
        FilterState state = filters.containsKey(name) ? filters.get(name).left() : FilterState.UNSET;
        return new Items.ClickItem(new ItemBuilder(state.item)
                .name(Translation.component(player.locale(), "shop.filter." + state.name().toLowerCase()).color(state.color))
                .addLore(Translation.component(player.locale(), "shop.filter.lore-" + state.name().toLowerCase()))
                .build(), (p, e) -> {
            if (e.isLeftClick() && state != FilterState.YES) {
                filters.put(name, Pair.of(FilterState.YES, superClass::isInstance));
            } else if (e.isRightClick() && state != FilterState.NO) {
                filters.put(name, Pair.of(FilterState.NO, obj -> !superClass.isInstance(obj)));
            } else {
                filters.remove(name);
            }
            player.closeInv();
            filterUsageMenu(f, name.split("\\.")[0]);
        });
    }

    public ItemStack filter(Material item, String name) {
        return new ItemBuilder(item)
                .name(Translation.component(player.locale(), "shop.filter").color(Colors.ACCENT)
                        .append(Component.text(" - ", Colors.EXTRA))
                        .append(Translation.component(player.locale(), "info." + name + "." + name).color(Colors.SECONDARY)))
                .build();
    }

    private Turn<?> turn(@NotNull Map<ItemStack, Turn<?>> map, Material material) {
        for (Map.Entry<ItemStack, Turn<?>> entry : map.entrySet()) {
            if (entry.getKey().getType() == material) return entry.getValue();
        }
        return Turn.DUMMY;
    }
}
