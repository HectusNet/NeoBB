package net.hectus.bb.shop;

import net.hectus.bb.turn.Turn;
import net.hectus.bb.util.ItemBuilder;
import net.hectus.bb.util.ItemLoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class ShopItemUtilities {
    public static final List<Material> ITEMS = Arrays.stream(Material.values()).filter(m -> getTurn(m) != null).toList();

    public static int getPrice(@NotNull Material material) {
        return switch (material) {
            case DRAGON_HEAD, PHANTOM_SPAWN_EGG -> 5;
            case PINK_BED, BLUE_BED, SHEEP_SPAWN_EGG, EVOKER_SPAWN_EGG, IRON_SHOVEL -> 4;
            case GREEN_BED, SOUL_SAND -> 3;
            default -> 2;
        };
    }

    public static @Nullable Turn getTurn(@NotNull Material material) {
        for (Turn turn : Turn.values()) {
            if (turn.items.contains(material))
                return turn;
        }
        return null;
    }

    public static ItemStack item(Locale l, Material m) {
        return new ItemBuilder(m)
                .lore(ItemLoreBuilder.of(m, ShopItemUtilities.getPrice(m), Objects.requireNonNull(ShopItemUtilities.getTurn(m))).build(l))
                .build();
    }
}
