package net.hectus.bb.shop;

import net.hectus.bb.turn.Turn;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class ShopItemUtilities {
    public static final List<Material> ITEMS = Arrays.stream(Material.values()).filter(m -> getPrice(m) != 0).toList();

    public static int getPrice(@NotNull Material material) {
        return switch (material) {
            case SPRUCE_TRAPDOOR -> 2;
            case CAULDRON, GREEN_CARPET -> 3;
            case IRON_TRAPDOOR, GOLD_BLOCK -> 4;
            case BLACK_WOOL -> 6;
            case SCULK -> 7;
            case PURPLE_WOOL -> 12;
            default -> 0;
        };
    }

    public static @Nullable Turn getTurn(@NotNull Material material) {
        for (Turn turn : Turn.values()) {
            if (turn.items.contains(material))
                return turn;
        }
        return null;
    }
}
