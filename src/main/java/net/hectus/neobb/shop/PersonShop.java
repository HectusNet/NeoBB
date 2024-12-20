package net.hectus.neobb.shop;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.util.ItemBuilder;
import net.hectus.neobb.shop.util.Items;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.person_game.block.*;
import net.hectus.neobb.turn.person_game.categorization.*;
import net.hectus.neobb.turn.person_game.structure.PTCandleCircle;
import net.hectus.neobb.turn.person_game.structure.PTTorchCircle;
import net.hectus.neobb.turn.person_game.warp.PTAmethystWarp;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PersonShop extends Shop {
    private final BiConsumer<Map<ItemStack, Turn<?>>, ItemStack> addConsumer = (it, i) -> {
        Turn<?> turn = turn(it, i.getType());
        try {
            if (player.inventory.allowItem(turn, i.getType()))
                player.inventory.addToDeck(i, turn);
        } catch (IndexOutOfBoundsException ignored) {}
    };

    private final PagedGui<Item> gui;

    public PersonShop(@NotNull NeoPlayer player) {
        super(player);

        Locale l = player.locale();

        gui = PagedGui.items().setStructure(
                        "# 0 1 2 3 4 5 6 #", // # = Border
                        "# 7 8 9 a . . . #", // 0-a = Categories
                        "# # # # # # # # #", // * = Items
                        "* * * * * * * * *", // D = Done Button
                        "* * * * * * * * *", // < = Last Page
                        "< # # # D # # # >") // > = Next Page
                .setBackground(Items.GRAY_BACKGROUND).addIngredient('#', Items.WHITE_BACKGROUND)

                .addIngredient('0', category(l, new PTCandleCircle(player), t -> t instanceof ArmorCategory))
                .addIngredient('1', category(l, new PTBeeNest(player), t -> t instanceof AttackCategory))
                .addIngredient('2', category(l, new PTCake(player), t -> t instanceof BuffCategory))
                .addIngredient('3', category(l, new PTAmethystBlock(player), t -> t instanceof CounterCategory))
                .addIngredient('4', category(l, new PTBlueStainedGlass(player), t -> t instanceof DefensiveCategory))
                .addIngredient('5', category(l, new PTLever(player), t -> t instanceof DefensiveCounterCategory))
                // TODO: .addIngredient('6', category(l, new PTCandleCircle(player), t -> t instanceof GameEndingCounterCategory))
                .addIngredient('7', category(l, new PTTorchCircle(player), t -> t instanceof SituationalAttackCategory))
                .addIngredient('8', category(l, new PTGlowstone(player), t -> t instanceof UtilityCategory))
                .addIngredient('9', category(l, new PTAmethystWarp(player.game.world()), t -> t instanceof WarpCategory))
                .addIngredient('a', category(l, new PTBrainCoral(player), t -> t instanceof WinConCategory))

                .addIngredient('D', new Items.ClickItem(new ItemBuilder(Material.LIME_DYE)
                        .name(Translation.component(l, "shop.done.name").color(Colors.ACCENT).decorate(TextDecoration.BOLD))
                        .addLore(Translation.component(l, "shop.done.lore.1").color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, false))
                        .addLore(Translation.component(l, "shop.done.lore.2").color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, false))
                        .build(), (p, e) -> player.closeInv()))
                .addIngredient('<', new Items.PageItem(false))
                .addIngredient('>', new Items.PageItem(true))

                .addIngredient('I', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .build();
    }

    @Override
    public void open() {
        Window.single().setTitle("=== Shop ===").addCloseHandler(() -> Bukkit.getScheduler().runTask(NeoBB.PLUGIN, () -> {
            if (player.player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST) done();
        })).setGui(gui).open(player.player);
    }

    public void setContent(Predicate<Turn<?>> filter) {
        gui.setContent(content(dummyTurns.stream()
                .filter(filter)
                .flatMap(t -> t.items().stream().map(i -> Pair.of(new ItemBuilder(i).lore(loreBuilder.turn(t).buildWithTooltips(player.locale())).build(), t)))
                .sorted(Comparator.comparing(p -> PlainTextComponentSerializer.plainText().serialize(p.left().displayName())))
                .collect(Collectors.toMap(Pair::left, Pair::right, (e, r) -> e, LinkedHashMap::new))));
        open();
    }

    public List<Item> content(@NotNull LinkedHashMap<ItemStack, Turn<?>> items) {
        return items.sequencedKeySet().stream().map(i -> (Item) new Items.ClickItem(i, (p, e) -> addConsumer.accept(items, i))).toList();
    }

    private Turn<?> turn(@NotNull Map<ItemStack, Turn<?>> map, Material material) {
        for (Map.Entry<ItemStack, Turn<?>> entry : map.entrySet()) {
            if (entry.getKey().getType() == material) return entry.getValue();
        }
        return Turn.DUMMY;
    }

    private Items.@NotNull ClickItem category(Locale l, @NotNull Category example, Predicate<Turn<?>> filter) {
        return new Items.ClickItem(new ItemBuilder(example.categoryItem())
                .name(Translation.component(l, "info.function." + example.categoryName()))
                .build(), (p, e) -> setContent(filter));
    }
}
