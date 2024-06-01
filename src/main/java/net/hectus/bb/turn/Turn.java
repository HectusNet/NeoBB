package net.hectus.bb.turn;

import com.marcpg.libpg.lang.Translatable;
import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.turn.buff.Buff;
import net.hectus.bb.turn.buff.ChanceBuff;
import net.hectus.bb.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static net.hectus.bb.turn.Turn.ItemClass.*;
import static net.hectus.bb.turn.Turn.ItemFunction.*;
import static net.hectus.bb.turn.Turn.ItemType.*;

public enum Turn implements Translatable {
    PURPLE_WOOL(false, null, null, BLOCK, ATTACK, NEUTRAL, Material.PURPLE_WOOL),
    SPRUCE_TRAPDOOR(false, null, List.of(NEUTRAL, WATER_CLASS, NATURE), BLOCK, COUNTER, NEUTRAL, Material.SPRUCE_TRAPDOOR),
    IRON_TRAPDOOR(false, null, List.of(NEUTRAL, WATER_CLASS, REDSTONE), BLOCK, COUNTERATTACK, NEUTRAL, Material.IRON_TRAPDOOR),
    CAULDRON(false, null, null, BLOCK, ATTACK, NEUTRAL, Material.CAULDRON),
    GOLD_BLOCK(false, null, List.of(NEUTRAL, HOT, NATURE), BLOCK, COUNTERATTACK, NEUTRAL, Material.GOLD_BLOCK),
    BLACK_WOOL(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.BLINDNESS)), List.of(COLD, SUPERNATURAL), BLOCK, COUNTERATTACK, NEUTRAL, Material.BLACK_WOOL),
    SCULK(false, List.of(new Buff.Luck(Buff.Target.OPPONENT, -10), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.DARKNESS)), List.of(NEUTRAL, SUPERNATURAL), BLOCK, COUNTERATTACK, NEUTRAL, Material.SCULK),
    GREEN_CARPET(false, null, List.of(CounterFilter.of(last -> last.turn().name().endsWith("_WOOL"), "wool")), BLOCK, COUNTERATTACK, NEUTRAL, Material.GREEN_CARPET),
    STONECUTTER(false, List.of(new Buff.ExtraTurn()), List.of(CounterFilter.of(last -> last.player().opponent().getDefense().left() && Objects.requireNonNull(last.player().opponent().getDefense().right()).isWall, "walls")), BLOCK, COUNTERATTACK, NEUTRAL, Material.STONECUTTER),
    MAGENTA_GLAZED_TERRACOTTA(true, null, List.of(NEUTRAL, HOT, COLD, WATER_CLASS, NATURE), BLOCK, COUNTERATTACK, NEUTRAL, Material.MAGENTA_GLAZED_TERRACOTTA),
    CYAN_CARPET(false, List.of(new Buff.ExtraTurn()), List.of(REDSTONE, SUPERNATURAL), BLOCK, COUNTER_OR_BUFF, NEUTRAL, Material.CYAN_CARPET),
    SPONGE(true, List.of(new Buff.ExtraTurn(), new Buff.Luck(Buff.Target.YOU, 5)), List.of(NEUTRAL, WATER_CLASS, SUPERNATURAL), BLOCK, COUNTERBUFF, NEUTRAL, Material.SPONGE),
    MAGMA_BLOCK(true, null, null, BLOCK, ATTACK, HOT, Material.MAGMA_BLOCK),
    NETHERRACK(false, null, List.of(HOT, COLD, WATER_CLASS), BLOCK, COUNTERATTACK, HOT, Material.NETHERRACK),
    LAVA(true, null, null, BLOCK, BUFF, HOT, Material.LAVA_BUCKET),
    FIRE(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.Target.BOTH, PotionEffectType.GLOWING)), List.of(COLD, REDSTONE, NATURE), BLOCK, COUNTER, HOT, Material.FLINT_AND_STEEL),
    ORANGE_WOOL(false, null, null, BLOCK, COUNTERATTACK, HOT, Material.ORANGE_WOOL),
    CAMPFIRE(true, null, List.of(NEUTRAL, HOT, COLD), BLOCK, COUNTERATTACK, HOT, Material.CAMPFIRE),
    RESPAWN_ANCHOR(true, List.of(new Buff.Teleport(Buff.Target.YOU), new ChanceBuff(new Buff.ExtraTurn(), 50)), null, BLOCK, BUFF, HOT, Material.RESPAWN_ANCHOR),
    PACKED_ICE(false, null, null, BLOCK, ATTACK, COLD, Material.PACKED_ICE),
    BLUE_ICE(false, null, List.of(COLD, WATER_CLASS, SUPERNATURAL), BLOCK, COUNTER, COLD, Material.BLUE_ICE),
    SPRUCE_LEAVES(false, null, List.of(NEUTRAL, NATURE), BLOCK, COUNTERATTACK, COLD, Material.SPRUCE_LEAVES),
    LIGHT_BLUE_WOOL(false, null, null, BLOCK, ATTACK, COLD, Material.LIGHT_BLUE_WOOL),
    WHITE_WOOL(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.SLOW, 99)), List.of(NEUTRAL, WATER_CLASS, REDSTONE, SUPERNATURAL), BLOCK, COUNTERBUFF, COLD, Material.WHITE_WOOL),
    POWDER_SNOW(false, null, null, BLOCK, ATTACK, COLD, Material.POWDER_SNOW_BUCKET),
    BRAIN_CORAL_BLOCK(false, null, List.of(REDSTONE), BLOCK, COUNTERATTACK, WATER_CLASS, Material.BRAIN_CORAL_BLOCK),
    HORN_CORAL(false, null, List.of(NEUTRAL, NATURE, WATER_CLASS), BLOCK, COUNTERATTACK, WATER_CLASS, Material.HORN_CORAL),
    FIRE_CORAL(false, null, List.of(HOT, REDSTONE), BLOCK, COUNTERATTACK, WATER_CLASS, Material.FIRE_CORAL),
    FIRE_CORAL_FAN(false, List.of(new ChanceBuff(new Buff.ExtraTurn(), 80)), List.of(HOT, REDSTONE), BLOCK, COUNTERBUFF, WATER_CLASS, Material.FIRE_CORAL_FAN),
    SEA_LANTERN(false, null, null, BLOCK, BUFF, WATER_CLASS, Material.SEA_LANTERN),
    WATER(false, null, List.of(CounterFilter.of(last -> last.turn().itemStacks.get(0).getType().createBlockData() instanceof Waterlogged, "waterloggable")), BLOCK, COUNTER, WATER_CLASS, Material.WATER_BUCKET),
    DRIED_KELP_BLOCK(false, List.of(new Buff.Effect(Buff.Target.YOU, PotionEffectType.JUMP, 2)), null, BLOCK, BUFF, WATER_CLASS, Material.DRIED_KELP_BLOCK),
    VERDANT_FROGLIGHT(false, null, List.of(SUPERNATURAL), BLOCK, COUNTERATTACK, COLD, Material.VERDANT_FROGLIGHT),
    BEE_NEST(false, null, null, BLOCK, ATTACK, NATURE, Material.BEE_NEST),
    HONEY_BLOCK(false, null, List.of(NATURE, WATER_CLASS, REDSTONE), BLOCK, COUNTER, NATURE, Material.HONEY_BLOCK),
    GREEN_WOOL(false, null, List.of(NATURE, HOT, COLD), BLOCK, COUNTERATTACK, NATURE, Material.GREEN_WOOL),
    MANGROVE_ROOTS(false, null, List.of(NEUTRAL, WATER_CLASS, NATURE), BLOCK, COUNTER, NATURE, Material.MANGROVE_ROOTS),
    COMPOSTER(true, null, null, BLOCK, ATTACK, NATURE, Material.COMPOSTER),
    HAY_BLOCK(false, null, List.of(COLD, WATER_CLASS), BLOCK, COUNTERATTACK, NATURE, Material.HAY_BLOCK),
    OAK_STAIRS(false, List.of(new Buff.ExtraTurn()), List.of(HOT, NATURE, SUPERNATURAL), BLOCK, COUNTERBUFF, NATURE, Material.OAK_STAIRS),
    // TODO: LEVER(true, null, List.of(NEUTRAL, REDSTONE), BOTH, BLOCK, COUNTER, REDSTONE, Material.LEVER),
    FENCE_GATE(false, null, List.of(NEUTRAL, HOT), BLOCK, COUNTERATTACK, REDSTONE, Material.OAK_FENCE_GATE),
    PISTON(true, null, List.of(CounterFilter.of(last -> true, "all")), BLOCK, COUNTER, REDSTONE, Material.PISTON),
    REPEATER(false, null, List.of(NEUTRAL, COLD, NATURE), BLOCK, COUNTERATTACK, REDSTONE, Material.REPEATER),
    LIGHTNING_ROD(false, null, List.of(NEUTRAL, HOT, REDSTONE, SUPERNATURAL), BLOCK, COUNTERATTACK, REDSTONE, Material.LIGHTNING_ROD),
    // TODO: OBSERVER(false, null, null, BOTH, BLOCK, EVENT, REDSTONE, Material.OBSERVER),
    // TODO: WOODEN_BUTTON(false, null, List.of(HOT, COLD, WATER), BOTH, BLOCK, COUNTER, REDSTONE, Material.OAK_BUTTON, Material.BIRCH_BUTTON, Material.DARK_OAK_BUTTON, Material.BAMBOO_BUTTON, Material.ACACIA_BUTTON, Material.CHERRY_BUTTON),
    // TODO: STONE_BUTTON(false, null, List.of(NATURE, REDSTONE, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, REDSTONE, Material.STONE_BUTTON),
    RED_CARPET(false, List.of(new Buff.ExtraTurn()), List.of(NEUTRAL, COLD, REDSTONE), BLOCK, COUNTERBUFF, REDSTONE, Material.RED_CARPET),
    RED_BED(false, null, List.of(NEUTRAL, COLD, SUPERNATURAL), BLOCK, COUNTERATTACK, SUPERNATURAL, Material.RED_BED),
    PINK_BED(false, null, null, BLOCK, BUFF, SUPERNATURAL, Material.PINK_BED),
    GREEN_BED(false, null, List.of(NEUTRAL, NATURE), BLOCK, COUNTERATTACK, SUPERNATURAL, Material.GREEN_BED),
    BLUE_BED(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), List.of(COLD, WATER_CLASS, NATURE, SUPERNATURAL), BLOCK, COUNTERBUFF, SUPERNATURAL, Material.BLUE_BED),
    DRAGON_HEAD(false, List.of(new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.SLOW), new Buff.Luck(Buff.Target.OPPONENT, -20)), null, BLOCK, BUFF, SUPERNATURAL, Material.DRAGON_HEAD),
    SOUL_SAND(false, null, null, BLOCK, ATTACK, SUPERNATURAL, Material.SOUL_SAND),
    CHORUS_FRUIT(false, null, null, ITEM, EVENT, NEUTRAL, Material.CHORUS_FRUIT),
    IRON_SHOVEL(true, List.of(new Buff.ExtraTurn()), List.of(CounterFilter.of(last -> last.turn() == CAULDRON, "cauldron"), HOT), ITEM, COUNTERBUFF, NEUTRAL, Material.IRON_SHOVEL),
    ENDER_PEARL(false, List.of(new Buff.ExtraTurn()), List.of(CounterFilter.of(last -> last.player().opponent().getDefense().left() && Objects.requireNonNull(last.player().opponent().getDefense().right()).isWall, "walls"), CounterFilter.of(last -> last.player().player().hasPotionEffect(PotionEffectType.LEVITATION), "levitation")), THROWABLE, ItemFunction.OTHER, NEUTRAL, Material.ENDER_PEARL),
    SNOWBALL(false, List.of(new Buff.ExtraTurn(), new Buff.Luck(Buff.Target.YOU, 15), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.GLOWING)), List.of(HOT), THROWABLE, COUNTER_OR_BUFF, COLD, Material.SNOWBALL),
    SPLASH_WATER_BOTTLE(false, List.of(new Buff.Luck(Buff.Target.YOU, 20), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.SLOW), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.BLINDNESS)), null, THROWABLE, COUNTER_OR_BUFF, WATER_CLASS, new ItemBuilder(Material.SPLASH_POTION)
            .editMeta(meta -> ((PotionMeta) meta).setBasePotionType(PotionType.WATER))
            .build()),
    SPLASH_LEVITATION_POTION(false, List.of(new Buff.ExtraTurn()), null, THROWABLE, BUFF, SUPERNATURAL, new ItemBuilder(Material.SPLASH_POTION)
            .editMeta(meta -> {
                ((PotionMeta) meta).addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 0, true), true);
                ((PotionMeta) meta).setColor(Color.WHITE);
            })
            .build()),
    SPLASH_JUMP_BOOST_POTION(false, null, null, THROWABLE, BUFF, SUPERNATURAL, new ItemBuilder(Material.SPLASH_POTION)
            .editMeta(meta -> ((PotionMeta) meta).setBasePotionType(PotionType.JUMP))
            .build()),
    BLAZE(false, null, null, MOB, ATTACK, HOT, Material.BLAZE_SPAWN_EGG),
    PIGLIN(true, List.of(new Buff.Effect(Buff.Target.YOU, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.Target.YOU, PotionEffectType.SLOW), new Buff.Luck(Buff.Target.YOU, -30)), null, ItemType.OTHER, EVENT, HOT, Material.PIGLIN_SPAWN_EGG),
    POLAR_BEAR(false, List.of(new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.SPEED, 2)), null, MOB, BUFF, COLD, Material.POLAR_BEAR_SPAWN_EGG),
    AXOLOTL(false, List.of(new Buff.Luck(Buff.Target.OPPONENT, -10), new Buff.Luck(Buff.Target.OPPONENT, 10)), null, MOB, BUFF, WATER_CLASS, Material.AXOLOTL_SPAWN_EGG),
    BOAT(true, null, null, MOB, EVENT, WATER_CLASS, Material.OAK_BOAT),
    BEE(false, null, null, MOB, BUFF, NATURE, Material.BEE_SPAWN_EGG),
    SHEEP(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, MOB, BUFF, SUPERNATURAL, Material.SHEEP_SPAWN_EGG),
    PHANTOM(false, null, null, MOB, ATTACK, SUPERNATURAL, Material.PHANTOM_SPAWN_EGG),
    EVOKER(false, List.of(new Buff.Luck(Buff.Target.YOU, 20), new Buff.ExtraTurn()), List.of(CounterFilter.of(last -> last.entity() instanceof Sheep sheep && sheep.getColor() == DyeColor.BLUE, "blue-sheep")), MOB, COUNTER_OR_BUFF, SUPERNATURAL, Material.EVOKER_SPAWN_EGG),
    OAK_DOOR_TURTLING(true, null, null, STRUCTURE, DEFENSE, NEUTRAL, new ItemStack(Material.OAK_DOOR, 4)),
    // TODO: IRON_BAR_JAIL(true, null, null, STRUCTURE, BUFF, NATURE, new ItemStack(Material.IRON_BARS, 4)),
    PUMPKIN_WALL(true, null, null, STRUCTURE, BUFF, NATURE, new ItemStack(Material.CARVED_PUMPKIN, 14)),
    // TODO: REDSTONE_WALL(true, null, null, STRUCTURE, BUFF, NATURE, new ItemStack(Material.REDSTONE_BLOCK, 6)),
    DAYLIGHT_SENSOR_LINE(true, null, List.of(HOT, NATURE, WATER_CLASS, REDSTONE), STRUCTURE, COUNTERBUFF, REDSTONE, new ItemStack(Material.DAYLIGHT_DETECTOR, 7)),
    GLASS_WALL(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, STRUCTURE, DEFENSE, NEUTRAL, new ItemStack(Material.GLASS, 6)),
    ORANGE_GLASS_WALL(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, STRUCTURE, DEFENSE, HOT, new ItemStack(Material.ORANGE_STAINED_GLASS, 6)),
    WHITE_GLASS_WALL(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, STRUCTURE, DEFENSE, COLD, new ItemStack(Material.WHITE_STAINED_GLASS, 6)),
    BLUE_GLASS_WALL(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, STRUCTURE, DEFENSE, WATER_CLASS, new ItemStack(Material.BLUE_STAINED_GLASS, 6)),
    GREEN_GLASS_WALL(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, STRUCTURE, DEFENSE, NATURE, new ItemStack(Material.GREEN_STAINED_GLASS, 6)),
    RED_GLASS_WALL(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, STRUCTURE, DEFENSE, REDSTONE, new ItemStack(Material.RED_STAINED_GLASS, 6)),
    PINK_GLASS_WALL(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, STRUCTURE, DEFENSE, SUPERNATURAL, new ItemStack(Material.PINK_STAINED_GLASS, 6)),
    NOTE_BLOCK(true, List.of(new Buff.Luck(Buff.Target.YOU, 10)), List.of(CounterFilter.of(last -> last.block() != null && last.block().isSolid(), "solid")), BLOCK, COUNTERBUFF, REDSTONE, Material.NOTE_BLOCK),
    // ========== FLOWERS ==========
    DIRT(false, null, null, BLOCK, ItemFunction.OTHER, NATURE, Material.DIRT),
    FLOWER_POT(false, null, null, BLOCK, ItemFunction.OTHER, NATURE, Material.FLOWER_POT),
    FLOWER_POPPY(false, null, null, BLOCK, BUFF, NATURE, Material.POPPY),
    FLOWER_BLUE_ORCHID(false, null, null, BLOCK, BUFF, NATURE, Material.BLUE_ORCHID),
    FLOWER_ALLIUM(false, null, null, BLOCK, BUFF, NATURE, Material.ALLIUM),
    FLOWER_AZURE_BLUET(false, List.of(new Buff.Luck(Buff.Target.YOU, 20)), null, BLOCK, BUFF, NATURE, Material.AZURE_BLUET),
    FLOWER_RED_TULIP(false, List.of(new Buff.ExtraTurn()), null, BLOCK, BUFF, NATURE, Material.RED_TULIP),
    FLOWER_ORANGE_TULIP(false, List.of(new Buff.ExtraTurn()), null, BLOCK, BUFF, NATURE, Material.ORANGE_TULIP),
    FLOWER_WHITE_TULIP(false, List.of(new Buff.ExtraTurn()), null, BLOCK, BUFF, NATURE, Material.WHITE_TULIP),
    FLOWER_PINK_TULIP(false, List.of(new Buff.ExtraTurn()), null, BLOCK, BUFF, NATURE, Material.PINK_TULIP),
    FLOWER_CORNFLOWER(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.DARKNESS)), null, BLOCK, BUFF, NATURE, Material.CORNFLOWER),
    FLOWER_OXEYE_DAISY(false, List.of(new ChanceBuff(new Buff.ExtraTurn(), 25)), null, BLOCK, BUFF, NATURE, Material.OXEYE_DAISY),
    FLOWER_WITHER_ROSE(false, List.of(new Buff.Luck(Buff.Target.OPPONENT, -15)), null, BLOCK, BUFF, NATURE, Material.WITHER_ROSE),
    FLOWER_SUNFLOWER(false, List.of(new Buff.Luck(Buff.Target.YOU, 10), new Buff.Effect(Buff.Target.BOTH, PotionEffectType.SPEED, 1)), null, BLOCK, BUFF, NATURE, Material.SUNFLOWER),
    FLOWER_OAK_SAPLING(false, null, null, BLOCK, BUFF, NATURE, Material.OAK_SAPLING),
    FLOWER_SPORE_BLOSSOM(false, null, null, ItemType.OTHER, BUFF, NATURE, Material.SPORE_BLOSSOM),
    // ========== FLOWERS ==========
    NETHER_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.NETHERRACK, 4), new ItemStack(Material.MAGMA_BLOCK)),
    END_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.END_STONE, 4), new ItemStack(Material.OBSIDIAN)),
    OCEAN_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.DARK_PRISMARINE, 4), new ItemStack(Material.SEA_LANTERN)),
    FROZEN_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.SNOW_BLOCK, 4), new ItemStack(Material.BLUE_ICE)),
    MUSHROOM_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.BROWN_MUSHROOM_BLOCK, 4), new ItemStack(Material.RED_MUSHROOM_BLOCK)),
    ClIFF_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.STONE, 4), new ItemStack(Material.SNOW_BLOCK)),
    MEADOW_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.GRASS_BLOCK, 4), new ItemStack(Material.EMERALD_ORE)),
    VOID_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.BLACK_STAINED_GLASS, 4), new ItemStack(Material.BLACK_CONCRETE)),
    REDSTONE_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.REDSTONE_LAMP, 4), new ItemStack(Material.REDSTONE_BLOCK)),
    WOOD_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.OAK_LEAVES, 4), new ItemStack(Material.OAK_LOG)),
    DESERT_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.SAND, 4), new ItemStack(Material.SUSPICIOUS_SAND)),
    NERD_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.LECTERN, 4), new ItemStack(Material.BOOKSHELF)),
    AMETHYST_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.AMETHYST_BLOCK, 4), new ItemStack(Material.BUDDING_AMETHYST)),
    SUN_WARP(true, null, null, STRUCTURE, EVENT, ItemClass.OTHER, new ItemStack(Material.VERDANT_FROGLIGHT, 4), new ItemStack(Material.SHROOMLIGHT));

    public final boolean usage;
    public final List<Buff> buffs;
    public final List<CounterFilter> countering;
    public final ItemType type;
    public final ItemFunction function;
    public final ItemClass clazz;
    public final List<ItemStack> itemStacks;

    Turn(boolean usage, @Nullable List<Buff> buffs, @Nullable List<CounterFilter> countering, ItemType type, ItemFunction function, ItemClass clazz, Material... items) {
        this(usage, buffs, countering, type, function, clazz, Arrays.stream(items).map(ItemStack::new).toArray(ItemStack[]::new));
    }

    Turn(boolean usage, @Nullable List<Buff> buffs, @Nullable List<CounterFilter> countering, ItemType type, ItemFunction function, ItemClass clazz, ItemStack... items) {
        this.usage = usage;
        this.buffs = buffs;
        this.countering = countering;
        this.type = type;
        this.function = function;
        this.clazz = clazz;
        this.itemStacks = List.of(items);
    }

    @Override
    public String getTranslated(Locale locale) {
        return PlainTextComponentSerializer.plainText().serializeOr(getTranslatedComponent(locale), "Invalid Turn");
    }

    @Override
    public @NotNull Component getTranslatedComponent(Locale locale) {
        String key = "turns." + name().toLowerCase();
        if (key.equals(Translation.string(locale, key))) {
            return (Component.translatable(itemStacks.get(0).getType().translationKey()));
        } else {
            return Translation.component(locale, "turns." + name().toLowerCase());
        }
    }

    public List<Material> materials() {
        return itemStacks.stream().map(ItemStack::getType).toList();
    }

    public enum ItemType implements Translatable {
        BLOCK, ITEM, THROWABLE, MOB, STRUCTURE, OTHER;

        @Override
        public String getTranslated(Locale locale) {
            return Translation.string(locale, "info.type." + name().toLowerCase());
        }

        @Override
        public @NotNull Component getTranslatedComponent(Locale locale) {
            return Component.text(getTranslated(locale), color());
        }

        public TextColor color() {
            return switch (this) {
                case BLOCK -> NamedTextColor.BLUE;
                case ITEM -> NamedTextColor.RED;
                case THROWABLE -> NamedTextColor.LIGHT_PURPLE;
                case MOB -> NamedTextColor.GREEN;
                case STRUCTURE -> NamedTextColor.YELLOW;
                case OTHER -> NamedTextColor.GRAY;
            };
        }
    }

    public enum ItemFunction implements Translatable {
        ATTACK, COUNTER, COUNTERATTACK, COUNTERBUFF, COUNTER_OR_BUFF, BUFF, DEFENSE, EVENT, OTHER;

        @Override
        public String getTranslated(Locale locale) {
            return Translation.string(locale, "info.function." + name().toLowerCase());
        }

        @Override
        public @NotNull Component getTranslatedComponent(Locale locale) {
            return Component.text(getTranslated(locale), color());
        }

        public TextColor color() {
            return switch (this) {
                case ATTACK -> NamedTextColor.RED;
                case COUNTER -> NamedTextColor.YELLOW;
                case COUNTERATTACK, COUNTERBUFF -> NamedTextColor.GOLD;
                case COUNTER_OR_BUFF -> NamedTextColor.GREEN;
                case BUFF -> NamedTextColor.DARK_GREEN;
                case DEFENSE -> NamedTextColor.BLUE;
                case EVENT -> NamedTextColor.LIGHT_PURPLE;
                case OTHER -> NamedTextColor.GRAY;
            };
        }
    }

    public enum ItemClass implements Translatable, CounterFilter {
        NEUTRAL, HOT, COLD, WATER_CLASS, NATURE, REDSTONE, SUPERNATURAL, OTHER;

        @Override
        public String getTranslated(Locale locale) {
            return Translation.string(locale, "info.class." + name().toLowerCase());
        }

        @Override
        public @NotNull Component getTranslatedComponent(Locale locale) {
            return Component.text(getTranslated(locale), color());
        }

        public TextColor color() {
            return switch (this) {
                case HOT -> NamedTextColor.GOLD;
                case COLD -> NamedTextColor.WHITE;
                case WATER_CLASS -> NamedTextColor.BLUE;
                case NATURE -> NamedTextColor.GREEN;
                case REDSTONE -> NamedTextColor.RED;
                case SUPERNATURAL -> NamedTextColor.YELLOW;
                default -> NamedTextColor.GRAY;
            };
        }

        @Override
        public boolean doCounter(@Nullable TurnData last) {
            return last != null && last.turn().clazz == this;
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "info.class." + name().toLowerCase());
        }
    }
}
