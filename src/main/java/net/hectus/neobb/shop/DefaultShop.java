package net.hectus.neobb.shop;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.lore.ItemLoreBuilder;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.ItemBuilder;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.util.TriConsumer;
import xyz.xenondevs.invui.window.Window;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultShop extends Shop {
    private static final TriConsumer<NeoPlayer, Map<ItemStack, Turn<?>>, ItemStack> buyConsumer = (p, it, i) -> {
        Turn<?> turn = turn(it, i.getType());
        if (p.inventory.removeCoins(turn.cost())) {
            try {
                if (p.inventory.allowItem(turn, i.getType())) {
                    p.inventory.addToDeck(i, turn);
                    return;
                }
            } catch (IndexOutOfBoundsException ignored) {}
            p.inventory.addCoins(turn.cost());
        }
        p.player.playSound(p.player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    };

    public DefaultShop(@NotNull Game game, ItemLoreBuilder loreBuilder) {
        super(game, loreBuilder);
    }

    @Override
    public void open(@NotNull NeoPlayer player) {
        shop(player, t -> true);
    }

    public void shop(@NotNull NeoPlayer player, Predicate<Turn<?>> filter) {
        Locale l = player.locale();

        LinkedHashMap<ItemStack, Turn<?>> items = turns.stream()
                .map(c -> turn(c, player))
                .filter(filter)
                .flatMap(t -> t.items().stream().map(i -> Pair.of(new ItemBuilder(i).lore(loreBuilder.turn(t).build(l)).build(), t)))
                .sorted(Comparator.comparing(p -> PlainTextComponentSerializer.plainText().serialize(p.left().displayName())))
                .collect(Collectors.toMap(Pair::left, Pair::right, (e, r) -> e, LinkedHashMap::new));

        Window.single()
                .setTitle("=== Shop ===")
                .addCloseHandler(() -> done(player))
                .setGui(ScrollGui.items().setStructure(
                                "F # # # # # # # ^", // # = Border
                                "F # I I I I I I #", // F = Filters
                                "F # I I I I I I #", // I = Items
                                "F # I I I I I I #", // D = Done Button
                                "F # I I I I I I #", // ^ = Scroll Up
                                "F # # # D # # # v") // v = Scroll Down
                        .addIngredient('#', new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
                        .addIngredient('F', new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
                        .addIngredient('I', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                        .addIngredient('D', new Items.ClickItem(new ItemBuilder(Material.LIME_DYE)
                                .name(Translation.component(l, "shop.done.name").color(Colors.ACCENT).decorate(TextDecoration.BOLD))
                                .addLore(Translation.component(l, "shop.done.lore.1").color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, false))
                                .addLore(Translation.component(l, "shop.done.lore.2").color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, false))
                                .build(), (p, e) -> p.closeInventory()))
                        .addIngredient('^', new Items.ScrollItem(true))
                        .addIngredient('v', new Items.ScrollItem(false))
                        .setContent(items.sequencedKeySet().stream().map(i -> (Item) new Items.ClickItem(i, (p, e) -> buyConsumer.accept(player, items, i))).toList())
                        .build())
                .open(player.player);
    }

    private static Turn<?> turn(@NotNull Map<ItemStack, Turn<?>> map, Material material) {
        for (Map.Entry<ItemStack, Turn<?>> entry : map.entrySet()) {
            if (entry.getKey().getType() == material) return entry.getValue();
        }
        return Turn.DUMMY;
    }
}
