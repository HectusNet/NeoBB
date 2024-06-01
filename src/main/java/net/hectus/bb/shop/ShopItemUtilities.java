package net.hectus.bb.shop;

import net.hectus.bb.BlockBattles;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.util.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ShopItemUtilities {
    public static final NamespacedKey TURN_KEY = new NamespacedKey(BlockBattles.getPlugin(BlockBattles.class), "turn");
    public static final List<ItemStack> ITEM_STACKS = new ArrayList<>();

    static {
        for (Turn turn : Turn.values()) {
            for (ItemStack item : turn.itemStacks) { // Adds the item stack and sets the custom turn value in the persistent data container to the turn.
                ITEM_STACKS.add(new ItemBuilder(item).editMeta(meta -> meta.getPersistentDataContainer().set(TURN_KEY, PersistentDataType.STRING, turn.name())).build());
            }
        }
    }

    public static int getPrice(@NotNull ItemStack item) {
        Turn turn = getTurn(item);
        return switch (item.getType()) {
            case IRON_BARS, ALLIUM, SPORE_BLOSSOM, OBSERVER -> 7;
            case LAVA, PISTON, LIGHTNING_ROD, PUFFERFISH, OAK_BOAT, DAYLIGHT_DETECTOR, POPPY, BLUE_ORCHID -> 6;
            case GOLD_BLOCK, SPONGE, CAMPFIRE, WHITE_WOOL, POWDER_SNOW_BUCKET, FIRE_CORAL_FAN, HONEY_BLOCK, GREEN_WOOL,
                 MANGROVE_ROOTS, OAK_STAIRS, LEVER, OAK_BUTTON, BIRCH_BUTTON, DARK_OAK_BUTTON, BAMBOO_BUTTON,
                 ACACIA_BUTTON, CHERRY_BUTTON, RED_CARPET, DRAGON_HEAD, IRON_SHOVEL, BLAZE_SPAWN_EGG, PHANTOM_SPAWN_EGG,
                 CARVED_PUMPKIN, GLASS, ORANGE_STAINED_GLASS, WHITE_STAINED_GLASS, BLUE_STAINED_GLASS, GREEN_STAINED_GLASS,
                 RED_STAINED_GLASS, PINK_STAINED_GLASS, PINK_TULIP, CORNFLOWER, WITHER_ROSE, SUNFLOWER -> 5;
            case PURPLE_WOOL, SPRUCE_TRAPDOOR, IRON_TRAPDOOR, SCULK, STONECUTTER, MAGENTA_GLAZED_TERRACOTTA, CYAN_CARPET,
                 FLINT_AND_STEEL, ORANGE_WOOL, RESPAWN_ANCHOR, BLUE_ICE, SPRUCE_LEAVES, HORN_CORAL, FIRE_CORAL, BEE_NEST,
                 COMPOSTER, OAK_FENCE_GATE, REPEATER, RED_BED, PINK_BED, BLUE_BED, POLAR_BEAR_SPAWN_EGG, AXOLOTL_SPAWN_EGG,
                 SHEEP_SPAWN_EGG, EVOKER_SPAWN_EGG, OAK_DOOR, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, OAK_SAPLING,
                 BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK, GRASS_BLOCK, EMERALD_ORE, REDSTONE_LAMP, AMETHYST_BLOCK,
                 BUDDING_AMETHYST, VERDANT_FROGLIGHT, SHROOMLIGHT -> 4;
            case BLACK_WOOL, GREEN_CARPET, PACKED_ICE, LIGHT_BLUE_WOOL, WATER_BUCKET, DRIED_KELP_BLOCK, HAY_BLOCK,
                 STONE_BUTTON, GREEN_BED, SOUL_SAND, CHORUS_FRUIT, PIGLIN_SPAWN_EGG, BEE_SPAWN_EGG, AZURE_BLUET,
                 OXEYE_DAISY, NOTE_BLOCK, END_STONE, OBSIDIAN, DARK_PRISMARINE, SNOW_BLOCK, STONE, OAK_LEAVES, OAK_LOG,
                 SAND, SUSPICIOUS_SAND, LECTERN, BOOKSHELF -> 3;
            case MAGMA_BLOCK, NETHERRACK -> turn == Turn.NETHER_WARP ? 3 : 4; // Warp : Turn
            case SEA_LANTERN -> turn == Turn.OCEAN_WARP ? 3 : 8; // Warp : Turn
            case REDSTONE_BLOCK -> turn == Turn.REDSTONE_WARP ? 4 : 6; // Warp : Turn
            default -> 2;
        };
    }

    public static Turn getTurn(@NotNull ItemStack item) {
        return Turn.valueOf(item.getItemMeta().getPersistentDataContainer().get(TURN_KEY, PersistentDataType.STRING));
    }
}
